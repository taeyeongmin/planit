package com.square.planit.holiday.controller;

import com.square.planit.holiday.dto.HolidayRefreshReq;
import com.square.planit.holiday.dto.HolidaySearchReq;
import com.square.planit.holiday.dto.HolidaySearchRes;
import com.square.planit.holiday.service.HolidayQueryService;
import com.square.planit.holiday.service.HolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/holiday")
public class HolidayApiController {

    private final HolidayService holidayService;
    private final HolidayQueryService holidayQueryService;

    @PostMapping("/refresh")
    public void refreshHolidays(@Valid @RequestBody HolidayRefreshReq holidayRefreshReq) {
        holidayService.upsertHoliday(holidayRefreshReq);
    }

    @GetMapping("/search")
    public Page<HolidaySearchRes> search(HolidaySearchReq req, Pageable pageable) {
        return holidayQueryService.getHolidaysByScope(req, pageable);
    }
}
