package com.sk.svdonation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SVDonationBackend {
    private static final Logger logger = LoggerFactory.getLogger(SVDonationBackend.class);

    public static void main(String[] args) {
        logger.info("start sv-donation-backend");
        SpringApplication.run(SVDonationBackend.class, args);
    }
}