package com.square.planit.init;

import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.service.CountryService;
import com.square.planit.holiday.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayInitializerServiceImpl implements  HolidayInitializerService {

    private final CountryService countryService;
    private final HolidayService holidayService;

    private static final int YEARS_RANGE  = 5;

    @Override
    public void initializeAllData() {

        int startYear = getStartYear();
        int endYear = getEndYear();

        List<Country> countries = countryService.initCountries();

        for (int year = startYear; year <= endYear; year++) {
            for (Country country : countries) {
                try {
                    holidayService.initHoliday(year, country);
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }

    private int getStartYear() {
        return LocalDate.now().getYear() - YEARS_RANGE;
    }

    private int getEndYear() {
        return LocalDate.now().getYear();
    }
}