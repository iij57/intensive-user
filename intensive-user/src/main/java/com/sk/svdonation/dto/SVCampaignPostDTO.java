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
public class SVCampaignPostDTO {
    private String campaignTitle;
    private String campaignContent;
    private String campaignImageUrl;
    private String campaignStartAt;
    private String campaignFinishAt;
    private long targetTokenAmount;
    private String contractData;
    private String signedTx;
}