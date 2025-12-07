package com.square.planit.init;

import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.service.CountryService;
import com.square.planit.holiday.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayInitializerService {

    private final CountryService countryService;
    private final HolidayService holidayService;

    private static final int START_YEAR = 2020;
    private static final int END_YEAR = 2025;

    @Transactional
    public void initializeAllData() {

        List<Country> countries = countryService.initCountries();

        for (int year = START_YEAR; year <= END_YEAR; year++) {
            for (Country country : countries) {
                try {
                    holidayService.initHoliday(year, country);
//                    holidayService.upsertHoliday(year, country);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }
}