package com.square.planit.holiday.entity;

import com.square.planit.holiday.enums.HolidayType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(
    name = "holiday_scope",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_holiday_scope_holiday_county_type",
            columnNames = {"holiday_id", "county_code", "holiday_type"}
        )
    },
    indexes = {
        @Index(name = "idx_holiday_scope_county", columnList = "county_code"),
        @Index(name = "idx_holiday_scope_type", columnList = "holiday_type")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HolidayScope {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scope_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "holiday_id", nullable = false)
    private Holiday holiday;

    @Column(name = "county_code", length = 20)
    private String countyCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type", nullable = false, length = 30)
    private HolidayType type;

    public HolidayScope(String countyCode, HolidayType type) {
        this.countyCode = countyCode;
        this.type = type;
    }

    void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HolidayScope)) return false;
        HolidayScope hs = (HolidayScope) o;
        return Objects.equals(countyCode, hs.countyCode)
            && type == hs.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(countyCode, type);
    }
}
