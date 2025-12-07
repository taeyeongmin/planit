package com.square.planit.holiday.service;

import com.square.planit.holiday.dto.HolidayModifyReq;
import com.square.planit.holiday.exception.NotFoundCountryException;
import com.square.planit.holiday.repository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private HolidayService holidayService;


    @Test
    void 예외_존재하지_않는_국가에대한_최신화(){

        // given
        HolidayModifyReq req = new HolidayModifyReq("KR", 2025);

        // when
        when(countryRepository.findById(any())).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundCountryException.class, () -> holidayService.upsertHoliday(req));

    }

}