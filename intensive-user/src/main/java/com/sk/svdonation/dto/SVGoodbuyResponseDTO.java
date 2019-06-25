package com.sk.svdonation.dto;

import com.sk.svdonation.entity.SVGoodbuyStatus;

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
public class SVGoodbuyResponseDTO {
	private long buyId;
	private String productName;
	private String memberId;
    private long svcAmount;
    private long svpAmount;
    private String createAt;
    private SVGoodbuyStatus productStatus;
}