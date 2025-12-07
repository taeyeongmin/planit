package com.square.planit.holiday.service;

import com.square.planit.holiday.dto.HolidaySearchReq;
import com.square.planit.holiday.dto.HolidaySearchRes;
import com.square.planit.holiday.entity.HolidayScope;
import com.square.planit.holiday.repository.HolidayScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HolidayQueryService {

    private final HolidayScopeRepository holidayScopeRepository;

    @Transactional(readOnly = true)
    public Page<HolidaySearchRes> getHolidaysByScope(HolidaySearchReq cond, Pageable pageable) {

        Page<HolidayScope> scopes = holidayScopeRepository.searchScopes(cond, pageable);

        return scopes.map(HolidaySearchRes::from);
    }
}
