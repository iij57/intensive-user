package com.sk.svdonation.controller;

import java.util.HashMap;
import java.util.Map;

import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.dto.SVCampaignPostDTO;
import com.sk.svdonation.service.SVCampaignService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class SVCampaignController {
    private SVCampaignService campaignService;

    @GetMapping("/v1/campaigns")
    public ResponseDTO getCampaigns() {
        log.info("CALL API SVCampaignController.getCampaigns");
        return new ResponseDTO.Builder().data(campaignService.getCampaigns()).build();
    }
    
    @GetMapping("/v1/campaigns/noorder")
    public ResponseDTO getCampaignsNoOrder() {
        log.info("CALL API SVCampaignController.getCampaigns");
        return new ResponseDTO.Builder().data(campaignService.getCampaignsNoOrder()).build();
    }

    @GetMapping("/v1/campaigns/{campaignId}")
    public ResponseDTO getCampaign(@PathVariable("campaignId") long campaignId) {
        log.info("CALL API SVCampaignController.getCampaign {campaignId: " + campaignId + "}");
        return new ResponseDTO.Builder().data(campaignService.getCampaign(campaignId)).build();
    }
    
    @GetMapping("/v1/campaigns/{campaignId}/nodetails")
    public ResponseDTO getCampaignNodetails(@PathVariable("campaignId") long campaignId) {
        log.info("CALL API SVCampaignController.getCampaignNodetails {campaignId: " + campaignId + "}");
        return new ResponseDTO.Builder().data(campaignService.getCampaignNodetails(campaignId)).build();
    }

    @PostMapping("/v1/campaigns")
    // @PreAuthorize("hasAnyAuthority('ROLE_FOUNDATION')")
    // public ResponseDTO createCampaign(@RequestBody SVCampaignDTO campaign, Authentication authentication) {
    //     UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    //     String userName = userDetails.getUsername();
    //     log.info("CALL SVCampaignController.createCampaign {foundation : " + userName + "} {campaign: " + campaign + "}");
        
    //     long campaignId = campaignService.createCampaign(campaign, userName);
    public ResponseDTO createCampaign(@RequestBody SVCampaignPostDTO campaign) {
        log.info("CALL API SVCampaignController.createCampaign {foundation :  Foundation01} {campaign: " + campaign + "}");
        
        long campaignId = campaignService.createCampaign(campaign, "Foundation01");

        Map<String, Long> responseData = new HashMap<>();
        responseData.put("campaignId", campaignId);
        return new ResponseDTO.Builder().data(responseData).build();
    }
}