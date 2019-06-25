package com.sk.svdonation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.dto.SVGoodbuyDTO;
import com.sk.svdonation.service.SVGoodbuyService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class SVGoodbuyController {
	
	private SVGoodbuyService buyService;
	
    @PostMapping("/v1/goodbuy")
    public ResponseDTO create(@RequestBody SVGoodbuyDTO buy, Authentication authentication) {
    	log.info("CALL API SVGoodBuyController.create {goodBuy: " + buy +"}");
		
		 UserDetails userDetails = (UserDetails) authentication.getPrincipal(); 
		 String userName = userDetails.getUsername();
		 log.info("CALL API SVGoodBuyController.create {goodBuyUser: " + userName +"}");
		 
    	long buyId = buyService.create(buy, userName);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("goodbuy", buyId > 0);
        responseData.put("buyId", buyId);
        return new ResponseDTO.Builder().data(responseData).build();
    }
    
    @GetMapping("/v1/goodbuy/{memberId}/buy")
    public ResponseDTO getGoodbuyByMember(@PathVariable("memberId") String memberId) {
        log.info("CALL API SVGoodbuyController.getGoodbuyByMember {memberId: " + memberId + "}");
        return new ResponseDTO.Builder().data(buyService.getGoodbuyByMember(memberId)).build();
    }
    
    @GetMapping("/v1/goodbuy/{buyId}")
    public ResponseDTO getGoodbuyByBuyId(@PathVariable("buyId") long buyId) {
        log.info("CALL API SVGoodbuyController.getGoodbuyByBuyId {buyId: " + buyId + "}");
        return new ResponseDTO.Builder().data(buyService.getGoodbuyByBuyId(buyId)).build();
    }

}