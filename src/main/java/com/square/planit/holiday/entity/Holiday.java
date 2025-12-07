package com.square.planit.holiday.entity;

import com.square.planit.client.dto.HolidayRes;
import com.square.planit.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "holiday",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_holiday_country",
                        columnNames = {"country_code", "date", "name", "local_name"}
                )
        },
        indexes = {
                @Index(name = "idx_holiday_country_year", columnList = "country_code, holiday_year"),
                @Index(name = "idx_date", columnList = "date")
        }
)
@ToString
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

    @OneToMany(
            mappedBy = "holiday",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<HolidayScope> scopes = new ArrayList<>();

    private Holiday(Country country, HolidayRes hr) {
        this.country = country;
        this.date = hr.date();
        this.year = hr.date().getYear();
        this.localName = hr.localName();
        this.name = hr.name();
        this.fixed = hr.fixed();
        this.global = hr.global();
        this.launchYear = hr.launchYear();
    }

    public static Holiday create(Country country, HolidayRes hr) {
        return new Holiday(country, hr);
    }


    public void addScope(HolidayScope scope) {
        this.scopes.add(scope);
        scope.setHoliday(this);
    }

    public void removeScope(HolidayScope scope) {
        this.scopes.remove(scope);
        scope.setHoliday(null);
    }

    // refresh 필드 업데이트용
    public void updateBasicInfo(boolean fixed, Integer launchYear) {
        this.fixed = fixed;
        this.launchYear = launchYear;
    }

    public void clearScopes() {
        for (HolidayScope scope : new ArrayList<>(scopes)) {
            removeScope(scope);
        }
    }
}
