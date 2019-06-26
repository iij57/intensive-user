package com.sk.intensive.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sk.intensive.dto.UserDTO;
import com.sk.intensive.entity.UserEntity;
import com.sk.intensive.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
	
	private UserRepository userRepository;
	
	public void create(UserDTO user) {
		
		UserEntity userEntity = new UserEntity();
		
		userEntity.setUserId(user.getUserId());
		userEntity.setUserPassword(user.getUserPassword());
		userEntity.setUsername(user.getUserName());
		userEntity.setUserProfile(user.getUserProfile());
		
		userRepository.save(userEntity);
	}
	
	public List<UserEntity> getUsers() {
		
		log.info("get All Users");
		
		return userRepository.findAll();
		
	}
	
	public UserEntity getUsersByUserId(String userId) {
		
		log.info("get User");
		
		return userRepository.findByUserId(userId);
		
	}

}
