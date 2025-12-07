package com.square.planit.client.service;

import com.square.planit.client.dto.CountryRes;
import com.square.planit.client.dto.HolidayRes;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class NagerApiClient {

    private final WebClient webClient;

    public NagerApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("https://date.nager.at/api/v3")
                .build();
    }

    public List<CountryRes> getAvailableCountries() {
        return webClient.get()
                .uri("/AvailableCountries")
                .retrieve()
                .bodyToFlux(CountryRes.class)
                .collectList()
                .block();
    }

    public List<HolidayRes> getPublicHolidays(int year, String countryCode) {
        return webClient.get()
                .uri("/PublicHolidays/{year}/{countryCode}", year, countryCode)
                .retrieve()
                .bodyToFlux(HolidayRes.class)
                .collectList()
                .block();
    }
}
