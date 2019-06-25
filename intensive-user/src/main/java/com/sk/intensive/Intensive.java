package com.sk.intensive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Intensive {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		log.info("start intensive-user");
		SpringApplication.run(Intensive.class, args);

	}

}
