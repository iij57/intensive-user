package com.sk.intensive.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sk.intensive.entity.UserEntity;
import com.sk.intensive.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
	private UserRepository userRepository;
	
	public void create(UserEntity user) {
		
		userRepository.save(user);
	}
	
	public void update(UserEntity user) {
		
		UserEntity userEntity = userRepository.findByUserId(user.getUserId());
		
		userEntity.setUserPassword(user.getUserPassword());
		userEntity.setUsername(user.getUsername());
		userEntity.setUserProfile(user.getUserProfile());
		
		userRepository.save(userEntity);
	}
	
	public List<UserEntity> getUsers() {
		return userRepository.findAll();
		
	}
	
	public UserEntity getUsersByUserId(String userId) {
		
		
		return userRepository.findByUserId(userId);
		
	}
	
	public void delete(String UserId) {
		
		UserEntity userEntity = userRepository.findByUserId(UserId);
		
		userRepository.delete(userEntity);
		
	}

}
