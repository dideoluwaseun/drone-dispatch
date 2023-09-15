package com.oluwaseun.dronedispatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DroneDispatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DroneDispatchApplication.class, args);
    }

}
