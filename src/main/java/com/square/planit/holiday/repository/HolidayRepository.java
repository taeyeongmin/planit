package com.square.planit.holiday.repository;

import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Optional<Holiday> findByCountryCodeAndDateAndLocalNameAndName(String countryCode, LocalDate date, String localName, String name);

    Optional<Holiday> findByCountryAndDateAndLocalNameAndName(Country country, LocalDate date, String localName, String name);
}