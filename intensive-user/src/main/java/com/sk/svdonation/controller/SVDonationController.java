package com.sk.svdonation.controller;

import java.util.HashMap;
import java.util.Map;

import com.sk.svdonation.BroadCastConfig;
import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.dto.SVDonationPostDTO;
import com.sk.svdonation.service.SVDonationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class SVDonationController {
    private static final Logger logger = LoggerFactory.getLogger(SVDonationController.class);
    private SVDonationService donationService;
    
    @Autowired
    private BroadCastConfig broadcast;
    

    @GetMapping("/v1/campaigns/{campaignId}/donations")
    public ResponseDTO getDonationsByCampaign(@PathVariable("campaignId") long campaignId) {
        logger.info("CALL API SVDonationController.getDonationsByCampaign {campaignId: " + campaignId + "}");
        return new ResponseDTO.Builder().data(donationService.getDonations(campaignId)).build();
    }

    @GetMapping("/v1/members/{memberId}/donations")
    public ResponseDTO getDonationsByMember(@PathVariable("memberId") String memberId) {
        logger.info("CALL API SVDonationController.getDonationsByMember {memberId: " + memberId + "}");
        return new ResponseDTO.Builder().data(donationService.getDonations(memberId)).build();
    }

    @PostMapping("/v1/donations")
    public ResponseDTO donate(@RequestBody SVDonationPostDTO donation, Authentication authentication) {
        logger.info("CALL API SVDonationController.donate {donation: " + donation + "}");
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); String
		userName = userDetails.getUsername();
		logger.info("CALL API SVDonationController.donate {donateUser: " + userName +"}");
        
        boolean result = donationService.donate(donation, userName);
        Map<String, Boolean> responseData = new HashMap<>();
        responseData.put("donation", result);
        
        //기부완료시 소켓 통신
        broadcast.sendCampaigns();
        
        return new ResponseDTO.Builder().data(responseData).build();
    }
}