
package com.square.planit.holiday.dto;

import com.square.planit.holiday.enums.HolidayType;

import java.time.LocalDate;
import java.util.List;

public record HolidaySearchReq(
        String countryCode,
        Integer year,
        LocalDate from,
        LocalDate to,
        String county,
        List<HolidayType> types
) {}
