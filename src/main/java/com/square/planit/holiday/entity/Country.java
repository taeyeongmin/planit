package com.square.planit.holiday.entity;

import com.square.planit.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "country",
        uniqueConstraints = @UniqueConstraint(name = "uk_country_code", columnNames = "country_code")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Country extends BaseEntity {

    @Id
    @Column(name = "country_code", length = 2, nullable = false)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    public Country(String code, String name) {
        this.code = code;
        this.name = name;
    }
}