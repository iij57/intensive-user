package com.sk.intensive.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sk.intensive.entity.UserEntity;
import com.sk.intensive.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/v1/users")
	public void create(@RequestBody UserEntity user) {
		log.info("Call API UserController.create");
		userService.create(user);
	}
	
	@GetMapping("/v1/users")
	public List<UserEntity> getUsers(){
		log.info("Call API UserController.getUsers");
		return userService.getUsers();
	}
	
	@GetMapping("/v1/users/{userId}")
	public UserEntity getUsersByUserId(@PathVariable("userId") String userId){
		log.info("Call API UserController.getUsersByUserId");
		return userService.getUsersByUserId(userId);
	}
	
	@PutMapping("/v1/users")
	public void update(@RequestBody UserEntity user) {
		log.info("Call API UserController.update");
		userService.update(user);;
	}
	
	@DeleteMapping("/v1/users/{userId}")
	public void delete(@PathVariable("userId") String userId){
		log.info("Call API UserController.delete");
		userService.delete(userId);
		
	}
	
}
