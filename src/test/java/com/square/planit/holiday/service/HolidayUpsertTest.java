package com.square.planit.holiday.service;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.client.service.NagerApiClient;
import com.square.planit.holiday.dto.HolidayRefreshReq;
import com.square.planit.holiday.entity.Holiday;
import com.square.planit.holiday.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@Sql("/sql/holiday_test.sql")
class HolidayUpsertTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @MockitoBean
    private NagerApiClient apiClient;

    @Test
    @Transactional
    void upsert_holiday_기본정보변경(){

        // given
        List<HolidayRes> mockedApiResult = List.of(
                new HolidayRes(
                        LocalDate.of(2020, 1, 6),
                        "Heilige Drei Könige",
                        "Epiphany",
                        "CH",
                        true,
                        true,
                        List.of("CH-UR", "CH-ZH"),
                        1990,
                        List.of("OBSERVANCE", "PUBLIC")
                )
        );

        when(apiClient.getPublicHolidays(2020, "CH"))
                .thenReturn(mockedApiResult);

        HolidayRefreshReq req = new HolidayRefreshReq("CH",2020);

        // when
        holidayService.upsertHoliday(req);

        // then
        Holiday holiday = holidayRepository.findById(329L).orElseThrow();

        // launchYear 갱신 확인
        assertThat(holiday.getLaunchYear()).isEqualTo(1990);
    }

    @Test
    @Transactional
    void upsert_holiday_type변경(){

        // given
        List<HolidayRes> mockedApiResult = List.of(
                new HolidayRes(
                        LocalDate.of(2020, 1, 6),
                        "Heilige Drei Könige",
                        "Epiphany",
                        "CH",
                        true,
                        true,
                        List.of("CH-UR", "CH-SZ"),
                        null,
                        List.of("OBSERVANCE")
                )
                , new HolidayRes(
                        LocalDate.of(2020, 1, 6),
                        "Heilige Drei Könige",
                        "Epiphany",
                        "CH",
                        true,
                        true,
                        List.of("CH-TI"),
                        null,
                        List.of("OBSERVANCE", "PUBLIC","SCHOOL") // SCHOOL 타입 추가
                )
        );

        when(apiClient.getPublicHolidays(2020, "CH"))
                .thenReturn(mockedApiResult);

        HolidayRefreshReq req = new HolidayRefreshReq("CH",2020);

        // when
        holidayService.upsertHoliday(req);

        // then
        Holiday holiday = holidayRepository.findById(329L).orElseThrow();

        assertThat(holiday.getScopes().getLast().getTypes().size()).isEqualTo(3);
    }


    /**
     * 기존 두개의 row로 조회 되던 공휴일에 대해
     * 한 row가 추가돼 총 3개의 row로 넘어오는 상황 가정
     */
    @Test
    @Transactional
    void upsert_holiday_counties추가(){

        // given
        List<HolidayRes> mockedApiResult = List.of(
                new HolidayRes(
                        LocalDate.of(2020, 1, 6),
                        "Heilige Drei Könige",
                        "Epiphany",
                        "CH",
                        true,
                        true,
                        List.of("CH-UR", "CH-SZ"),
                        null,
                        List.of("OBSERVANCE")
                )
                , new HolidayRes(
                        LocalDate.of(2020, 1, 6),
                        "Heilige Drei Könige",
                        "Epiphany",
                        "CH",
                        true,
                        true,
                        List.of("CH-TI"),
                        null,
                        List.of("OBSERVANCE", "PUBLIC","SCHOOL") // SCHOOL 타입 추가
                )
                ,  new HolidayRes(
                        LocalDate.of(2020, 1, 6),
                        "Heilige Drei Könige",
                        "Epiphany",
                        "CH-VS",
                        true,
                        true,
                        List.of("CH-TI"),
                        null,
                        List.of("PUBLIC","SCHOOL") // SCHOOL 타입 추가
                )
        );

        when(apiClient.getPublicHolidays(2020, "CH"))
                .thenReturn(mockedApiResult);

        HolidayRefreshReq req = new HolidayRefreshReq("CH",2020);

        // when
        holidayService.upsertHoliday(req);

        // then
        Holiday holiday = holidayRepository.findById(329L).orElseThrow();

        assertThat(holiday.getScopes().size()).isEqualTo(3);
    }

}