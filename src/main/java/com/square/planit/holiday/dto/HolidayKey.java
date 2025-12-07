package com.square.planit.holiday.dto;

import com.square.planit.holiday.entity.Country;

import java.time.LocalDate;

public record HolidayKey(
        String countryCode,
        LocalDate date,
        String localName,
        String name
) {
    public static HolidayKey of(Country country, LocalDate date, String localName, String name) {
        return new HolidayKey(country.getCode(), date, localName, name);
    }
}