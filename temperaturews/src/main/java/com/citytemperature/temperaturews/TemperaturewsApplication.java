package com.citytemperature.temperaturews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@ComponentScan
public class TemperaturewsApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemperaturewsApplication.class);

    public static void main(String[] args) {
        LOGGER.info("Starting services citytemperature.");
        SpringApplication.run(TemperaturewsApplication.class, args);
    }

}
