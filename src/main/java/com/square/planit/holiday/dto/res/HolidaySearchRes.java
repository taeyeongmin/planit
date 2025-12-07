package com.square.planit.holiday.dto;

import com.square.planit.holiday.entity.Holiday;
import com.square.planit.holiday.entity.HolidayScope;
import com.square.planit.holiday.enums.HolidayType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record HolidaySearchRes(
        LocalDate date,
        String localName,
        String name,
        String countryCode,
        boolean fixed,
        boolean global,
        List<String> counties,
        Integer launchYear,
        List<HolidayType> types
) {

    public static HolidaySearchRes from(HolidayScope scope) {
        Holiday h = scope.getHoliday();

        return new HolidaySearchRes(
                h.getDate(),
                h.getLocalName(),
                h.getName(),
                h.getCountry().getCode(),
                h.isFixed(),
                h.isGlobal(),
                new ArrayList<>(scope.getCounties()),
                h.getLaunchYear(),
                new ArrayList<>(scope.getTypes())
        );
    }
}
