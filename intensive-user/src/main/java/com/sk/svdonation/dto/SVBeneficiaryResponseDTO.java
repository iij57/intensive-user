package com.sk.svdonation.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@ToString
public class SVBeneficiaryResponseDTO {
    private String beneficiaryId;
    private long beneficiaryToken;
    private long campaignId;
    private SVMemberDTO beneficiaryMember;
}