package com.sk.svdonation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class SVMemberDTO {
    private String memberId;
    private String name;
    private String detail;
    private String memberType;
    private String walletAddress;
    private String privateKey;


    public SVMemberDTO(String memberId, String name, String memberType, String walletAddress, String privateKey) {
        this.memberId = memberId;
        this.name = name;
        this.detail = "";
        this.memberType = memberType;
        this.walletAddress = walletAddress;
        this.privateKey = privateKey;
    }    
}