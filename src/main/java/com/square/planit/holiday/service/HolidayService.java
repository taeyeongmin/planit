package com.square.planit.holiday.service;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.client.service.NagerApiClient;
import com.square.planit.holiday.dto.HolidayKey;
import com.square.planit.holiday.dto.HolidayRefreshReq;
import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.entity.Holiday;
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

        Country country = countryRepository.findById(holidayRefreshReq.getCountryCode()).orElseThrow(() -> new RuntimeException("Country code not found"));

        // API 호출
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
                if (hr.types() != null) holiday.getTypes().addAll(hr.types());
                if (hr.counties() != null) holiday.getCounties().addAll(hr.counties());
                holiday.updateBasicInfo(hr.fixed(), hr.launchYear());
            }
        }

        return new ArrayList<>(merged.values());
    }

    private void upsertMergedHolidays(List<Holiday> mergedHolidays) {

        for (Holiday newHoliday : mergedHolidays) {

            Optional<Holiday> optional = holidayRepository
                    .findByCountryAndDateAndLocalNameAndName(
                            newHoliday.getCountry(),
                            newHoliday.getDate(),
                            newHoliday.getLocalName(),
                            newHoliday.getName()
                    );

            if (optional.isEmpty()) {
                // 존재하지 않으면 신규 insert
                holidayRepository.save(newHoliday);
                continue;
            }

            // 존재하면 update
            Holiday existing = optional.get();

            // 기본 필드 업데이트
            existing.updateBasicInfo(newHoliday.isFixed(), newHoliday.getLaunchYear());

            // counties/types는 스냅샷 방식으로 덮어쓴다
            existing.getTypes().clear();
            existing.getCounties().clear();

            existing.getTypes().addAll(newHoliday.getTypes());
            existing.getCounties().addAll(newHoliday.getCounties());
        }
    }
}
