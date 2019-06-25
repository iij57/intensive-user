package com.sk.svdonation.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;

@Entity
@DiscriminatorValue("G")
@Getter
public class SVMemberGeneralEntity extends SVMemberBaseEntity {
    @Column(name = "MEMBER_NAME")
    private String memeberName;
    
    SVMemberGeneralEntity() {
    }
    
    private SVMemberGeneralEntity(String userId, String password, SVMemberType userType, String walletAddress, String privateKey, String memberName) {
        super(userId, password, userType, walletAddress, privateKey);
        this.memeberName = memberName;
    }

    public static SVMemberGeneralEntity create(String userId, String password, SVMemberType userType, String walletAddress, String privateKey, String memberName) {
        return new SVMemberGeneralEntity(userId, password, userType, walletAddress, privateKey, memberName);
    }

    public static SVMemberGeneralEntity create(String userId) {
        return create(userId, "", SVMemberType.DONATOR, "", "", "");
    }
}