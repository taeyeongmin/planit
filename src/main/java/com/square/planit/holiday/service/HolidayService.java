package com.square.planit.holiday.service;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.client.service.NagerApiClient;
import com.square.planit.holiday.dto.HolidayKey;
import com.square.planit.holiday.dto.HolidayRefreshReq;
import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.entity.Holiday;
import com.square.planit.holiday.entity.HolidayScope;
import com.square.planit.holiday.enums.HolidayType;
import com.square.planit.holiday.exception.NotFoundCountryException;
import com.square.planit.holiday.repository.CountryRepository;
import com.square.planit.holiday.repository.HolidayRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class HolidayService {


    private final NagerApiClient apiClient;
    private final CountryRepository countryRepository;
    private final HolidayRepository holidayRepository;

    @Transactional
    public void initHoliday(int year, Country country) {

        List<HolidayRes> apiData =
                apiClient.getPublicHolidays(year, country.getCode());

        List<Holiday> holidayList = mergeApiRowsToHolidayList(country, apiData);

        holidayRepository.saveAll(holidayList);
    }

    @Transactional
    public void upsertHoliday(HolidayRefreshReq holidayRefreshReq) {

        Country country = countryRepository.findById(holidayRefreshReq.getCountryCode())
                .orElseThrow(NotFoundCountryException::new);

        // 공휴일 api 호출
        List<HolidayRes> apiRows =
                apiClient.getPublicHolidays(holidayRefreshReq.getYear(), holidayRefreshReq.getCountryCode());

        // API row 를 Holiday 단위로 merge
        List<Holiday> mergedHolidays = mergeApiRowsToHolidayList(country, apiRows);

        // merge된 Holiday 리스트로 upsert 수행
        upsertMergedHolidays(mergedHolidays);
    }

    /**
     * 전달 받은 공휴일 API 로직을 Holiday key 기준 병합
     * @param country
     * @param apiRows
     * @return
     */
    private List<Holiday> mergeApiRowsToHolidayList(Country country, List<HolidayRes> apiRows) {


        Map<HolidayKey, Holiday> merged = new HashMap<>();

        for (HolidayRes hr : apiRows) {

            HolidayKey key = HolidayKey.of(country, hr.date(), hr.localName(), hr.name());
            Holiday holiday = merged.get(key);

            if (holiday == null) {
                holiday = Holiday.create(country, hr);
                merged.put(key, holiday);
            } else {
                // 기본 정보 갱신 (fixed, launchYear 등)
                holiday.updateBasicInfo(hr.fixed(), hr.launchYear());
            }

            // countiesm,types 조합을 HolidayScope로
            List<String> counties = (hr.counties() == null || hr.counties().isEmpty())
                            ? Collections.singletonList(null)
                            : hr.counties();
            List<String> types = hr.types() != null ? hr.types() : List.of();

            for (String typeStr : types) {
                HolidayType type = HolidayType.of(typeStr);

                for (String county : counties) {
                    HolidayScope scope = new HolidayScope(county, type);
                    holiday.addScope(scope);
                }
            }
        }

        return new ArrayList<>(merged.values());
    }

    private void upsertMergedHolidays(List<Holiday> mergedHolidays) {

        for (Holiday newHoliday : mergedHolidays) {

            Optional<Holiday> originHolidayOp = holidayRepository
                    .findByCountryAndDateAndLocalNameAndName(
                            newHoliday.getCountry(),
                            newHoliday.getDate(),
                            newHoliday.getLocalName(),
                            newHoliday.getName()
                    );
            if (originHolidayOp.isEmpty()) {

                holidayRepository.save(newHoliday);
                continue;
            }

            Holiday originHoliday = originHolidayOp.get();

            // 기본 필드 업데이트
            originHoliday.updateBasicInfo(newHoliday.isFixed(), newHoliday.getLaunchYear());

            // Scope 스냅샷 방식으로 덮어쓰기
            Set<HolidayScope> existingScopes = new HashSet<>(originHoliday.getScopes());
            Set<HolidayScope> newScopes = new HashSet<>(newHoliday.getScopes());

            // 제거할 것: 기존 - 신규
            Set<HolidayScope> toRemove = new HashSet<>(existingScopes);
            toRemove.removeAll(newScopes);

            // 추가할 것: 신규 - 기존
            Set<HolidayScope> toAdd = new HashSet<>(newScopes);
            toAdd.removeAll(existingScopes);

            toRemove.forEach(originHoliday::removeScope);
            toAdd.forEach(originHoliday::addScope);
        }
    }
}
