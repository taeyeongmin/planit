package com.square.planit.init;

import com.square.planit.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataLoader implements ApplicationRunner {

    private final HolidayInitializerService initializerService;

    @Override
    public void run(ApplicationArguments args) {

        System.out.println(">>>>>>>>>>> Initializing holiday data...");
        initializerService.initializeAllData();
        System.out.println(">>>>>>>>>>>> Initialization completed.");
    }
}