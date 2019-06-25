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
import lombok.Setter;

@Entity
@Table(name = "SV_VOTING")
@Getter
@Setter
public class SVVotingEntity extends SVBaseEntity{
	
	@Id
	@GeneratedValue
	@Column(name = "VOTE_ID", nullable = false , unique = true)
	private long voteId;
	
	@Column(name = "MEMBER_ID", length = 50)
	private String memberId;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CAMPAIGN", nullable = false)
    private SVCampaignEntity campaign;
	
	@Column(name = "SVP_AMOUNT", length = 20)
	private long svpAmount;
	
	//차후 enum으로 변경 현재 시나리오상 진행중인 P 만을 사용
	@Column(name = "CAMPAIGN_STATUS", length = 1)
	private String campaignStatus = "P";
	
	SVVotingEntity() {

	}
	
	private SVVotingEntity(String memberId, long campaignId, long svpAmount) {
		this.memberId = memberId;
		this.campaign = SVCampaignEntity.create(campaignId);
		this.svpAmount = svpAmount;
	}

	private SVVotingEntity(String memberId, SVCampaignEntity campaign, long svpAmount) {
		this.memberId = memberId;
		this.campaign = campaign;
		this.svpAmount = svpAmount;
	}
	
	public static SVVotingEntity create(String memberId, long campaignId, long svpAmount) {
		return new SVVotingEntity(memberId, campaignId, svpAmount);
	}

	public static SVVotingEntity create(String memberId, SVCampaignEntity campaign, long svpAmount) {
		return new SVVotingEntity(memberId, campaign, svpAmount);
	}
}
