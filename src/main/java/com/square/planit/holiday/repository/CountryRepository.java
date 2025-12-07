package com.square.planit.holiday.repository;

import com.square.planit.holiday.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, String> {}