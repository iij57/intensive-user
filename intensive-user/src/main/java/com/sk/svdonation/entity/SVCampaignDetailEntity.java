package com.sk.svdonation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "SV_CAMPAIGN_DETAIL")
@Getter
public class SVCampaignDetailEntity extends SVBaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "DETAIL_ID", nullable = false, unique = true)
    private long detailId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAMPAIGN", nullable = false)
    private SVCampaignEntity campaign;

    @Column(name = "DETAIL_INFO", length = 400, nullable = false)
    private String detailInfo;

    @Column(name = "DETAIL_SVC", nullable = false)
    private long detailSVC;

    @Enumerated(EnumType.STRING)
    @Column(name = "DETAIL_TYPE", length = 10, nullable = false)
    private SVCampaignDetailType detailType;

    SVCampaignDetailEntity() {

    }

    private SVCampaignDetailEntity(SVCampaignEntity campaign, String detailInfo, long detailSVC, SVCampaignDetailType detailType) {
        this.campaign = campaign;
        this.detailInfo = detailInfo;
        this.detailSVC = detailSVC;
        this.detailType = detailType;
    }
    
    public static SVCampaignDetailEntity create(SVCampaignEntity campaign, String detailInfo, long detailSVC, SVCampaignDetailType detailType) {
        return new SVCampaignDetailEntity(campaign, detailInfo, detailSVC, detailType);
    }
}