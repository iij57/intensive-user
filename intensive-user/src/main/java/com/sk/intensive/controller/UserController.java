package com.sk.intensive.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sk.intensive.dto.UserDTO;
import com.sk.intensive.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/v1/users")
	public void create(@RequestBody UserDTO user) {
		log.info("Call API UserController.create");
		userService.create(user);
	}
	

}
