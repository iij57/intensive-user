package com.sk.svdonation.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "SV_TOKEN_USAGE")
@Getter
public class SVTokenUsageEntity {
    @Id
    @GeneratedValue
    @Column(name = "TOKEN_USAGE_ID")
    private long tokenUsageId;

    @Column(name = "TOKEN_USAGE_AT")
    private LocalDateTime usageAt;
    
    @PrePersist
    private void prePersist() {
        this.usageAt = LocalDateTime.now();
    }

    @Column(name = "TOKEN_PAYMENT")
    private long payment;
    
    @Column(name = "TOKEN_PAYMENT_CATEGORY")
    private String category;
    

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TOKEN_BENEFICIARY", nullable = false)
    private SVBeneficiaryEntity beneficiary;

    SVTokenUsageEntity() {
    }

    private SVTokenUsageEntity(long payment, String category , SVBeneficiaryEntity beneficiary) {
        this.payment = payment;
        this.category = category;
        this.beneficiary = beneficiary;
    }

    public static SVTokenUsageEntity create(long payment, String category , SVBeneficiaryEntity beneficiary) {
        return new SVTokenUsageEntity(payment, category, beneficiary);
    }

}