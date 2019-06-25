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
@Table(name = "SV_BENEFICIARY")
@Getter
public class SVBeneficiaryEntity extends SVBaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "BENEFICIARY_ID")
    private long beneficiaryId;

    @Column(name = "BENEFICIARY_TOKEN")
    private long beneficiaryToken;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BENEFICIARY_MEMBER", nullable = false)
    private SVMemberGeneralEntity beneficiaryMember;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BENEFICIARY_CAMPAIGN", nullable = false)
    private SVCampaignEntity campaign;

    SVBeneficiaryEntity() {
    }

    private SVBeneficiaryEntity(long beneficiaryToken, SVMemberGeneralEntity beneficiaryMember, SVCampaignEntity campaign) {
        this.beneficiaryToken = beneficiaryToken;
        this.beneficiaryMember = beneficiaryMember;
        this.campaign = campaign;
    }

    public static SVBeneficiaryEntity create(long beneficiaryToken, SVMemberGeneralEntity beneficiaryMember, SVCampaignEntity campaign) {
        return new SVBeneficiaryEntity(beneficiaryToken, beneficiaryMember, campaign);
    }
}