package com.square.planit.holiday.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "holiday",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_holiday_country_date_name",
                columnNames = {"country_id", "date", "name"}
        ),
        indexes = {
                @Index(name = "idx_holiday_year_country", columnList = "holiday_year, country_id"),
                @Index(name = "idx_date_country", columnList = "date, country_id")
        }
)
public class Holiday {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "holiday_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id")
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

    protected Holiday() {}

    // 편의 생성자 API -> 엔티티 매핑용
    public Holiday(Country country,
                   LocalDate date,
                   String localName,
                   String name,
                   boolean fixed,
                   boolean global,
                   Integer launchYear) {
        this.country = country;
        this.date = date;
        this.year = date.getYear();  // 항상 동시에 세팅
        this.localName = localName;
        this.name = name;
        this.fixed = fixed;
        this.global = global;
        this.launchYear = launchYear;
    }
}
