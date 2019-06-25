package com.sk.svdonation.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@Entity
@DiscriminatorValue("F")
@Getter
public class SVMemberFoundationEntity extends SVMemberBaseEntity {
    @Column(name = "FOUNDATION_NAME")
    private String foundationName;

    SVMemberFoundationEntity() {
        
    }
    
    private SVMemberFoundationEntity(String userId, String password, SVMemberType userType, String walletAddress, String privateKey, String foundationName) {
        super(userId, password, userType, walletAddress, privateKey);
        this.foundationName = foundationName;
    }
    
    public static SVMemberFoundationEntity create(String userId, String password, SVMemberType userType, String walletAddress, String privateKey, String foundationName) {
        return new SVMemberFoundationEntity(userId, password, userType, walletAddress, privateKey, foundationName);
    }

    public static SVMemberFoundationEntity create(String userId) {
        return new SVMemberFoundationEntity(userId, "", null, "", "", "");
    }
}