package com.square.planit.holiday.service;

import com.square.planit.client.dto.CountryRes;
import com.square.planit.client.service.NagerApiClient;
import com.square.planit.holiday.entity.Country;
import com.square.planit.holiday.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;
    private final NagerApiClient apiClient;

    public List<Country> initCountries() {
        List<CountryRes> apiCountries = apiClient.getAvailableCountries();

        List<Country> saved = apiCountries.stream()
                .map(c -> {
                    // 이미 존재하면 skip
                    return countryRepository.findById(c.countryCode())
                            .orElse(new Country(c.countryCode(), c.name()));
                })
                .toList();

        return countryRepository.saveAll(saved);
    }
}
