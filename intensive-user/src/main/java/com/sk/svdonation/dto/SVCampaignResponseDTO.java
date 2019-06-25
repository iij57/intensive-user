package com.sk.svdonation.dto;

import java.util.Map;
import java.util.Set;

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
public class SVCampaignResponseDTO {
    private String campaignId;
    private String campaignAddress;
    private String campaignTitle;
    private String campaignContent;
    private String campaignImageUrl;
    private String campaignStartAt;
    private String campaignFinishAt;
    private long targetTokenAmount;
    private long usageTokenAmount;
    private long sumOfSVC;
    private long sumOfSVP;
    private String status;
    private SVMemberDTO foundation;
    private Set<SVDonationResponseDTO> donations;
    private Set<SVBeneficiaryResponseDTO> beneficiaries;
    //private Set<SVCampaignDetailResponseDTO> details;
    private Map<String, Object> details;
}