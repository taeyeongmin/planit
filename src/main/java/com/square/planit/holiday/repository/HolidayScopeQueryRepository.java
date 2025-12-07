package com.square.planit.holiday.repository;

import com.square.planit.holiday.dto.HolidaySearchReq;
import com.square.planit.holiday.entity.HolidayScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HolidayScopeQueryRepository {
    Page<HolidayScope> searchScopes(HolidaySearchReq cond, Pageable pageable);
}
