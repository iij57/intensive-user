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
public class SVGoodbuyDTO {
	private String productName;
    private long svcAmount;
   // private String svcAproveSignedTx;
    private String svcSignedTx;
    private long svpAmount;
    //private String svpApproveSignedTx;
    private String svpSignedTx;
}