package com.sk.svdonation.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "SV_MEMBER_BASE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@Getter
public abstract class SVMemberBaseEntity extends SVBaseEntity {
    @Id
    @Column(name = "MEMBER_ID", length = 50, nullable = false, unique = true)
    private String memberId;

    @Column(name = "MEMBER_PASSWORD", length = 100, nullable = false)
    private String password;

    @Column(name = "MEMBER_TYPE", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private SVMemberType memberType;

    @Column(name = "MEMBER_WALLET_ADDRESS", length = 100)
    private String walletAddress;

    @Column(name = "MEMBER_PRIVATE_KEY", length = 100)
    private String privateKey;

    SVMemberBaseEntity() {

    }

    SVMemberBaseEntity(String memberId, String password, SVMemberType memberType, String walletAddress, String privateKey) {
        this.memberId = memberId;
        this.password = password;
        this.memberType = memberType;
        this.walletAddress = walletAddress;
        this.privateKey = privateKey;
    }
    
}