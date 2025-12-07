package com.square.planit.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class InitialDataLoader implements ApplicationRunner {

    private final HolidayInitializerServiceImpl initializerService;

    @Override
    public void run(ApplicationArguments args) {
        initializerService.initializeAllData();
    }
}