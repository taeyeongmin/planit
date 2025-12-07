package com.square.planit.holiday.entity;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "holiday",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_holiday_business_key",
                        columnNames = {"country_code", "date", "local_name", "name"}
                )
        }
)
public class Holiday extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_code")
    private Country country;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "holiday_year", nullable = false)
    private Integer year;

    @Column(name = "local_name", nullable = false, length = 200)
    private String localName;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false)
    private boolean fixed;

    @Column(nullable = false)
    private boolean global;

    @Column(name = "launch_year")
    private Integer launchYear;

    @ElementCollection
    @CollectionTable(
            name = "holiday_type",
            joinColumns = @JoinColumn(name = "holiday_id")
    )
    @Column(name = "type", length = 50)
    private Set<String> types = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "holiday_county",
            joinColumns = @JoinColumn(name = "holiday_id")
    )
    @Column(name = "county_code", length = 20)
    private Set<String> counties = new HashSet<>();

    public static Holiday create(Country country, HolidayRes hr) {

        Holiday holiday = new Holiday();
        holiday.country = country;
        holiday.date = hr.date();
        holiday.year = hr.date().getYear();
        holiday.localName = hr.localName();
        holiday.name = hr.name();
        holiday.fixed = hr.fixed();
        holiday.global = hr.global();
        holiday.launchYear = hr.launchYear();

        if (hr.types() != null) holiday.types.addAll(hr.types());
        if (hr.counties() != null) holiday.counties.addAll(hr.counties());

        return holiday;
    }

    public void updateBasicInfo(boolean fixed, Integer launchYear) {
        this.fixed = fixed;
        this.launchYear = launchYear;
    }
}
