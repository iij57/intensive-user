package com.sk.svdonation.controller;

import java.util.HashMap;
import java.util.Map;

import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.dto.SVBeneficiaryPostDTO;
import com.sk.svdonation.service.SVBeneficiaryService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class SVBeneficiaryController {
    private static final Logger logger = LoggerFactory.getLogger(SVBeneficiaryController.class);
    private SVBeneficiaryService beneficiaryService;

    @GetMapping("/v1/beneficiaries/{campaignId}")
    public ResponseDTO getBeneficiaries(@PathVariable("campaignId") long campaignId) {
        logger.info("CALL API SVBeneficiaryController.getBeneficiaries {campaignId: " + campaignId + "}");
        return new ResponseDTO.Builder().data(beneficiaryService.getBeneficiaries(campaignId)).build();
    }
    
    @GetMapping("/v1/beneficiaries/{campaignId}/tokenusage")
    public ResponseDTO getBeneficiariesTokenUsageByCampaignId(@PathVariable("campaignId") long campaignId) {
        logger.info("CALL API SVBeneficiaryController.getBeneficiariesTokenUsageByCampaignId {campaignId: " + campaignId + "}");
        return new ResponseDTO.Builder().data(beneficiaryService.getBeneficiariesTokenUsageByCampaignId(campaignId)).build();
    }

    @PostMapping("/v1/beneficiaries/{memberId}")
    public ResponseDTO addBeneficiaries(@RequestBody SVBeneficiaryPostDTO beneficiary, @PathVariable String memberId) {
        logger.info("CALL API SVBeneficiaryController.addBeneficiaries {beneficiary :  " + beneficiary + ", memberId: " + memberId + "}");
        boolean result = beneficiaryService.addBeneficiary(beneficiary, memberId);
        Map<String, Boolean> responseData = new HashMap<>();
        responseData.put("beneficiary", result);
        return new ResponseDTO.Builder().data(responseData).build();
    }
    
}