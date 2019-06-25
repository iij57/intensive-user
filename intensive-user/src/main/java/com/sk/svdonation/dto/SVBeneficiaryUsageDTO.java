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
public class SVBeneficiaryUsageDTO {
    private String beneficiaryId;
    private String beneficiaryName;
    private long beneficiaryToken;
    private long tokenUsage;
    private long campaignId;
    private String category;
    private String tokenUsageAt;
    
}