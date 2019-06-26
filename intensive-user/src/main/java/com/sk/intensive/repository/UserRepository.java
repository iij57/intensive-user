package com.sk.intensive.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.sk.intensive.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, String>{

	UserEntity findByUserId(String userId);
	
	List<UserEntity> findAll();
	
}
