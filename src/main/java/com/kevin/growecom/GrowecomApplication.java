package com.kevin.growecom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GrowecomApplication {

    public static void main(String[] args) {
        SpringApplication.run(GrowecomApplication.class, args);
    }

}
