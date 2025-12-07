package com.square.planit.holiday.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.square.planit.holiday.dto.HolidaySearchReq;
import com.square.planit.holiday.entity.HolidayScope;
import com.square.planit.holiday.entity.QHoliday;
import com.square.planit.holiday.entity.QHolidayScope;
import com.square.planit.holiday.enums.HolidayType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HolidayScopeQueryRepositoryImpl implements HolidayScopeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<HolidayScope> searchScopes(HolidaySearchReq cond, Pageable pageable) {

        QHolidayScope scope = QHolidayScope.holidayScope;
        QHoliday holiday = QHoliday.holiday;

        // HolidayScope 기준
        List<HolidayScope> content = queryFactory
                .select(scope)
                .from(scope)
                .join(scope.holiday, holiday).fetchJoin() // Holiday 같이 조회
                .where(
                        countryEq(cond.countryCode(), holiday),
                        yearEq(cond.year(), holiday),
                        dateBetween(cond.from(), cond.to(), holiday),
                        typeIn(cond.types(), scope)
                )
                .orderBy(holiday.date.asc(), scope.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(scope.count())
                .from(scope)
                .join(scope.holiday, holiday)
                .where(
                        countryEq(cond.countryCode(), holiday),
                        yearEq(cond.year(), holiday),
                        dateBetween(cond.from(), cond.to(), holiday),
                        typeIn(cond.types(), scope)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression countryEq(String countryCode, QHoliday holiday) {
        return countryCode == null ? null : holiday.country.code.eq(countryCode);
    }

    private BooleanExpression yearEq(Integer year, QHoliday holiday) {
        return year == null ? null : holiday.year.eq(year);
    }

    private BooleanExpression dateBetween(LocalDate from, LocalDate to, QHoliday holiday) {
        if (from == null && to == null) return null;
        if (from != null && to != null) return holiday.date.between(from, to);
        if (from != null) return holiday.date.goe(from);
        return holiday.date.loe(to);
    }

    private BooleanExpression typeIn(List<HolidayType> types, QHolidayScope scope) {
        if (types == null || types.isEmpty()) return null;
        return scope.types.any().in(types);
    }
}
