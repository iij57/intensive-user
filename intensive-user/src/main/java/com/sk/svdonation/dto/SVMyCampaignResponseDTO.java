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
public class SVMyCampaignResponseDTO {
    private String campaignId;
    private String campaignAddress;
    private String campaignTitle;
    private String campaignImageUrl;
    private String campaignStartAt;
    private String campaignFinishAt;
    private long targetTokenAmount;
    private long usageTokenAmount;
    private long sumOfSVC;
    private long myDonateSVC;
    private long sumOfSVP;
    private String status;
    private SVMemberDTO foundation;
}