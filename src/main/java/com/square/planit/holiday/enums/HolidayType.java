package com.square.planit.holiday.enums;

public enum HolidayType {
    PUBLIC,
    BANK,
    SCHOOL,
    AUTHORITIES,
    OPTIONAL,
    OBSERVANCE;

    public static HolidayType of(String value) {
        return HolidayType.valueOf(value.toUpperCase());
    }
}