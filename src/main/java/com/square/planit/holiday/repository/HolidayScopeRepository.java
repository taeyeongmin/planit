package com.square.planit.holiday.repository;

import com.square.planit.holiday.entity.HolidayScope;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HolidayScopeRepository
        extends JpaRepository<HolidayScope, Long>, HolidayScopeQueryRepository {
}
