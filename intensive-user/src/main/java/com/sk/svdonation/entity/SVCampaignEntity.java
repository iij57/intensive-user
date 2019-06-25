package com.sk.svdonation.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

import lombok.Getter;

@Entity
@Table(name = "SV_CAMPAIGN")
@Getter
public class SVCampaignEntity extends SVBaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "CAMPAIGN_ID", nullable = false, unique = true)
    private long campaignId;

    @Column(name = "CAMPAIGN_ADDRESS", length = 200)
    private String campaignAddress;

    @Column(name = "CAMPAIGN_TITLE", length = 200, nullable = false)
    private String campaignTitle;

    @Lob
    @Column(name = "CAMPAIGN_CONTENT", nullable = false)
    private String campaignContent;

    @Column(name = "CAMPAIGN_IMG_URL", length = 200, nullable = false)
    private String campaignImageUrl;

    @Column(name = "CAMPAIGN_START_AT", nullable = false)
    private LocalDateTime campaignStartAt;

    @Column(name = "CAMPAIGN_FINISH_AT", nullable = false)
    private LocalDateTime campaignFinishAt;

    @Column(name = "TARGET_TOKEN_AMOUNT", nullable = false)
    private long targetTokenAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "CAMPAIGN_STATUS", length = 10, nullable = false)
    private SVCampaignStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAMPAIGN_FOUNDATION", nullable = false)
    private SVMemberFoundationEntity foundation;

    @BatchSize(size=10)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<SVDonationEntity> donations;
    
    @BatchSize(size=10)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<SVVotingEntity> voting;

    @BatchSize(size=10)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<SVBeneficiaryEntity> beneficiaries;

    @BatchSize(size=10)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "campaign", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<SVCampaignDetailEntity> details;

    SVCampaignEntity() {

    }

    private SVCampaignEntity(String campaignAddress, String campaignTitle, String campaignContent, String campaignImageUrl, LocalDateTime campaignStartAt, LocalDateTime campaignFinishAt, long targetTokenAmount, SVCampaignStatus status, SVMemberFoundationEntity foundation) {
        this.campaignAddress = campaignAddress;
        this.campaignTitle = campaignTitle;
        this.campaignContent = campaignContent;
        this.campaignImageUrl = campaignImageUrl;
        this.campaignStartAt = campaignStartAt;
        this.campaignFinishAt = campaignFinishAt;
        this.targetTokenAmount = targetTokenAmount;
        this.status = status;
        this.foundation = foundation;
        this.donations = new HashSet<>();
        this.voting = new HashSet<>();
    }

    public SVCampaignEntity(long campaignId) {
        this.campaignId = campaignId;
    }

    public static SVCampaignEntity create(String campaignAddress, String campaignTitle, String campaignContent, String campaignImageUrl, LocalDateTime campaignStartAt, LocalDateTime campaignFinishAt, long targetTokenAmount, SVCampaignStatus status, SVMemberFoundationEntity foundation) {
        return new SVCampaignEntity(campaignAddress, campaignTitle, campaignContent, campaignImageUrl, campaignStartAt, campaignFinishAt, targetTokenAmount, status, foundation);
    }

    public static SVCampaignEntity create(long campaignId) {
        return new SVCampaignEntity(campaignId);
    }

    public void updateCampaignAddress(String campaignAddress) {
        this.campaignAddress = campaignAddress;
    }
}