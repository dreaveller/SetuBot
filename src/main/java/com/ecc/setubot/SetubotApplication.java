package com.ecc.setubot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SetubotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SetubotApplication.class, args);
    }

}
