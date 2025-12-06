package com.square.planit.holiday.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "country",
        uniqueConstraints = @UniqueConstraint(name = "uk_country_code", columnNames = "country_id")
)
public class Country {

    @Id
    @Column(name = "country_id", length = 2, nullable = false)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

}