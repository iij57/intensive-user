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
public class SVVotingDTO {
	//private String memberId;
    private long campaignId;
    private long svpAmount;
    private String approveSignedTx;
    private String signedTx;
}