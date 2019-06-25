package com.sk.svdonation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sk.svdonation.BroadCastConfig;
import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.dto.SVVotingDTO;
import com.sk.svdonation.service.SVVotingService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class SVVotingController {
    private SVVotingService votingService;
    
    @Autowired
    private BroadCastConfig broadcast;
    
    @PostMapping("/v1/voting")
    public ResponseDTO create(@RequestBody SVVotingDTO voting, Authentication authentication) {
    	log.info("CALL API SVVotingController.create {vote: " + voting +"}");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal(); String
		userName = userDetails.getUsername();
		log.info("CALL API SVVotingController.create {votingUser: " + userName +"}");
		
    	boolean result = votingService.create(voting, userName);
        Map<String, Boolean> responseData = new HashMap<>();
        responseData.put("voting", result);
        
        //투표완료시 소켓 통신
        broadcast.sendCampaigns();
        
        return new ResponseDTO.Builder().data(responseData).build();
    }
    
    @GetMapping("/v1/voting/{campaignId}/vote")
    public ResponseDTO getVotingByCampaignId(@PathVariable("campaignId") long campaignId) {
        log.info("CALL API SVVotingController.getVotingByCampaignId {campaignId: " + campaignId + "}");
        return new ResponseDTO.Builder().data(votingService.getVotingByCampaignId(campaignId)).build();
    }

}