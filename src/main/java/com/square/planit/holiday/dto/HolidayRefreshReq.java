package com.square.planit.holiday.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HolidayRefreshReq {
    @NotBlank
    private String countryCode;
    @NotNull
    private Integer year;
}
