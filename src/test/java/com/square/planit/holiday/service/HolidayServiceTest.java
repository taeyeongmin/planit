package com.square.planit.holiday.service;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.client.service.NagerApiClient;
import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.entity.Holiday;
import com.square.planit.holiday.repository.CountryRepository;
import com.square.planit.holiday.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class HolidayServiceTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private CountryRepository countryRepository;

    @MockitoBean
    private NagerApiClient apiClient;

    @Test
    @Transactional
    void upsertHoliday_WhenApiReturnsChangedHoliday_ShouldUpdateExistingEntity() {

        // given
        int year = 2025;
        String countryCode = "KR";

        // Country 저장
        Country country = new Country(countryCode, "Korea");
        countryRepository.save(country);

        // 기존 Holiday 저장
        LocalDate date = LocalDate.of(year, 1, 1);

        HolidayRes hr = new HolidayRes(
                LocalDate.now()
                ,"대한민국"
                ,"korea"
                ,"KR"
                ,false
                ,false
                ,null
                ,null
                ,null
        );

        Holiday originHoliday = Holiday.create(country, hr);

        holidayRepository.save(originHoliday);

        Long oldId = originHoliday.getId();
//
//        // API 응답 Mocking
//        HolidayRes changed = new HolidayRes();
//        changed.setDate(date);
//        changed.setName("New Name");
//        changed.setLocalName("새 이름");
//
//        when(apiClient.getPublicHolidays(year, countryCode))
//                .thenReturn(List.of(changed));
//
//        HolidayRefreshReq req = new HolidayRefreshReq(year, countryCode);
//
//        // when
//        holidayService.upsertHoliday(req);
//
//        // then
//        List<Holiday> rows = holidayRepository.findAll();
//        assertThat(rows).hasSize(1);
//
//        Holiday updated = rows.get(0);
//        assertThat(updated.getId()).isEqualTo(oldId);
//        assertThat(updated.getName()).isEqualTo("New Name");
//        assertThat(updated.getLocalName()).isEqualTo("새 이름");
    }

}