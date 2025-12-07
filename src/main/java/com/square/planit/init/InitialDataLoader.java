package com.square.planit.init;

import com.square.planit.holiday.service.HolidayInitializerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements ApplicationRunner {

    private final HolidayInitializerServiceImpl initializerService;

    @Override
    public void run(ApplicationArguments args) {
        initializerService.initializeAllData();
    }
}