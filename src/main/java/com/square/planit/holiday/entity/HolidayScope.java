package com.square.planit.holiday.entity;

import com.square.planit.holiday.enums.HolidayType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "holiday_scope")
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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "holiday_scope_county",
            joinColumns = @JoinColumn(name = "scope_id")
    )
    @Column(name = "county_code", length = 20)
    @BatchSize(size = 100)
    private Set<String> counties = new LinkedHashSet<>();


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "holiday_scope_type",
            joinColumns = @JoinColumn(name = "scope_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "holiday_type", length = 30)
    @BatchSize(size = 100)
    private Set<HolidayType> types = new LinkedHashSet<>();

    public HolidayScope(Set<String> counties, Set<HolidayType> types) {
        if (counties != null) {
            this.counties.addAll(counties);
        }
        if (types != null) {
            this.types.addAll(types);
        }
    }

    void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

}
