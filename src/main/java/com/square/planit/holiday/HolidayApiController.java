package com.square.planit.holiday;

import com.square.planit.holiday.dto.HolidayRefreshReq;
import com.square.planit.holiday.service.HolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/holiday")
public class HolidayApiController {

    private final HolidayService holidayService;

    @PostMapping("/refresh")
    public void refreshHolidays(@Valid @RequestBody HolidayRefreshReq holidayRefreshReq) {
        holidayService.upsertHoliday(holidayRefreshReq);
    }
}
