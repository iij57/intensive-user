package com.sk.intensive.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="USER")
@Getter
@Setter
public class UserEntity {

	@Id
	@Column(name = "USER_ID", length = 100 , nullable = false , unique = true)
	private String userId;
	
	@Column(name = "USER_NAME", length = 100)
	private String userName;

	@Column(name = "USER_PASSWORD", length = 100)
	private String userPassword;

	@Column(name = "USER_PROFILE", length = 100)
	private String userProfile;

	
}
