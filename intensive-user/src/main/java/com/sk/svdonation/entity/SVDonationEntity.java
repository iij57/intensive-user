package com.sk.svdonation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "SV_DONATION")
@Getter
public class SVDonationEntity extends SVBaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "DONATION_ID", nullable = false, unique = true)
    private long donationId;

    @Column(name = "AMOUNT_OF_SVC", nullable = false)
    private long amountOfSVC;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DONATOR", nullable = false)
    private SVMemberGeneralEntity donator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAMPAIGN", nullable = false)
    private SVCampaignEntity campaign;

    SVDonationEntity() {

    }

    private SVDonationEntity(long amountOfSVC, SVMemberGeneralEntity donator, SVCampaignEntity campaign) {
        this.amountOfSVC = amountOfSVC;
        this.donator = donator;
        this.campaign = campaign;
    }

    public static SVDonationEntity create(long amountOfSVC, SVMemberGeneralEntity donator, SVCampaignEntity campaign) {
        return new SVDonationEntity(amountOfSVC, donator, campaign);
    }
}