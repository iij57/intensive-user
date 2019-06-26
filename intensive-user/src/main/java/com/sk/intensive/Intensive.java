package com.sk.intensive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan
public class Intensive {

    public static void main(String[] args) {
        log.info("start intensive-user");
        SpringApplication.run(Intensive.class, args);
    }
}