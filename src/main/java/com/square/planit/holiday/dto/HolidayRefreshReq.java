package com.square.planit.holiday.dto;

import lombok.Data;

@Data
public class HolidayRefreshReq {
    private String countryCode;
    private int year;
}
