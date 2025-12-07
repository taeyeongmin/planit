package com.square.planit.holiday.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@AllArgsConstructor
public class HolidayModifyReq {
    
    @NotBlank
    @Schema(description = "국가 코드", requiredMode = REQUIRED)
    private String countryCode;

    @NotNull
    @Schema(description = "연도", requiredMode = REQUIRED)
    private Integer year;
}
