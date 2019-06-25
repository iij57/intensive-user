package com.sk.svdonation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.java.Log;

@Log
@SpringBootApplication
@EnableScheduling
public class SVDonationBackend {

    public static void main(String[] args) {
        log.info("start sv-donation-backend");
        SpringApplication.run(SVDonationBackend.class, args);
    }
}