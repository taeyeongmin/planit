package com.square.planit.holiday.service;

import com.square.planit.holiday.dto.HolidayModifyReq;
import com.square.planit.holiday.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * 공휴일 삭제에 테스트로  holiday_test.sql를 통해
 * 기초 insert 후 진행한다.
 *  - country: 1건
 *  - Holiday: 1건
 */
@DirtiesContext
@SpringBootTest
@ActiveProfiles("test")
@Sql("/sql/holiday_test.sql")
class HolidayDeleteTest {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    @Transactional
    void 공휴일_삭제() {

        // given
        HolidayModifyReq holidayModifyReq = new HolidayModifyReq("CH", 2020);

        // when
        holidayService.deleteByCountryAndYear(holidayModifyReq);

        // then
        assertThat(holidayRepository.findAll().size()).isEqualTo(0);
    }
}