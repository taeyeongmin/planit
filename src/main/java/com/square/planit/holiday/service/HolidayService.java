package com.square.planit.holiday.service;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.client.service.NagerApiClient;
import com.square.planit.holiday.creator.HolidayEntityCreator;
import com.square.planit.holiday.dto.HolidayModifyReq;
import com.square.planit.holiday.dto.res.HolidayDeleteRes;
import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.entity.Holiday;
import com.square.planit.holiday.entity.HolidayScope;
import com.square.planit.holiday.exception.NotFoundCountryException;
import com.square.planit.holiday.repository.CountryRepository;
import com.square.planit.holiday.repository.HolidayRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final NagerApiClient apiClient;
    private final CountryRepository countryRepository;
    private final HolidayRepository holidayRepository;
    private final HolidayEntityCreator holidayEntityCreator;

    @Transactional
    public void initHoliday(int year, Country country) {

        List<HolidayRes> apiData =
                apiClient.getPublicHolidays(year, country.getCode());

        List<Holiday> holidayList = holidayEntityCreator.createHoliday(country, apiData);

        holidayRepository.saveAll(holidayList);
    }

    @Transactional
    public void upsertHoliday(HolidayModifyReq holidayModifyReq) {

        Country country = countryRepository.findById(holidayModifyReq.getCountryCode())
                .orElseThrow(NotFoundCountryException::new);

        // 공휴일 api 호출
        List<HolidayRes> apiRows =
                apiClient.getPublicHolidays(holidayModifyReq.getYear(), holidayModifyReq.getCountryCode());

        // 공휴의 api 응답 결과를 Holiday Entity로 변환
        List<Holiday> mergedHolidays = holidayEntityCreator.createHoliday(country, apiRows);

        // 생성 된 Holiday Entity로 upsert 수행
        upsertMergedHolidays(mergedHolidays);
    }

    @Transactional
    public HolidayDeleteRes deleteByCountryAndYear(HolidayModifyReq holidayModifyReq) {

        // 국가 존재 여부 검증
        Country country = countryRepository.findById(holidayModifyReq.getCountryCode())
                .orElseThrow(NotFoundCountryException::new);

        // 삭제 대상 조회
        List<Holiday> holidays = holidayRepository.findByCountryAndYear(country, holidayModifyReq.getYear());

        if (holidays.isEmpty()) {
            return new HolidayDeleteRes(holidayModifyReq, 0);
        }

        holidayRepository.deleteAll(holidays);

        return new HolidayDeleteRes(holidayModifyReq, holidays.size());
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

            // scope 덮어쓰기
            originHoliday.clearScopes();

            for (HolidayScope scope : newHoliday.getScopes()) {
                HolidayScope newScope = new HolidayScope(
                        new LinkedHashSet<>(scope.getCounties()),
                        new LinkedHashSet<>(scope.getTypes())
                );
                originHoliday.addScope(newScope);
            }
        }
    }
}
