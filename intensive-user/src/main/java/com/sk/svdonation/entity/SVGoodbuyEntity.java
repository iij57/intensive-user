package com.sk.svdonation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SV_GOODBUY")
@Getter
@Setter
public class SVGoodbuyEntity extends SVBaseEntity{
	
	@Id
	@GeneratedValue
	@Column(name = "BUY_ID", nullable = false , unique = true)
	private long buyId;
	
	@Column(name = "SVC_AMOUNT", nullable = false, length = 20)
	private long svcAmount;
	
	@Column(name = "SVP_AMOUNT", nullable = false, length = 20)
	private long svpAmount;
	
	@Column(name = "PRODUCT_NAME", nullable = false, length = 100)
	private String productName;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PRODUCT_STATUS", nullable = false, length = 50)
	private SVGoodbuyStatus productStatus;
	
	@Column(name = "MEMBER_ID", nullable = false, length = 50)
	private String memberId;
	
	SVGoodbuyEntity() {

	}
	
	private SVGoodbuyEntity(String memberId, String productName, long svcAmount, long svpAmount, SVGoodbuyStatus productStatus) {
		
		this.memberId = memberId;
		this.productName = productName;
		this.svcAmount = svcAmount;
		this.svpAmount = svpAmount;
		this.productStatus = productStatus;
		
	}
	
	public static SVGoodbuyEntity create(String memberId, String productName, long svcAmount, long svpAmount, SVGoodbuyStatus productStatus) {
		return new SVGoodbuyEntity(memberId, productName, svcAmount, svpAmount, productStatus);
	}
	

}
