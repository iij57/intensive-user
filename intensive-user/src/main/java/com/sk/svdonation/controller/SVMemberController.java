package com.sk.svdonation.controller;

import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.dto.SVMemberDTO;
import com.sk.svdonation.service.SVCampaignService;
import com.sk.svdonation.service.SVMemberService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
public class SVMemberController {
    private SVMemberService memberService;
    private SVCampaignService campaignService;

    @GetMapping("/v1/members/{memberId}")
    public ResponseDTO getMember(@PathVariable("memberId") String memberId, Authentication authentication) {
        log.info("CALL API SVMemberController.getMember {memberId: " + memberId + "}");
        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // String userName = userDetails.getUsername();
        // if(!memberId.equals(userName)) {
        //     throw new AccessDeniedException("권한이 없습니다.");
        // }
        SVMemberDTO memberDTO = memberService.getSVMember(memberId);
        ResponseDTO responseDTO = new ResponseDTO.Builder().data(memberDTO).build();
        return responseDTO;
    }

    
    @GetMapping("/v1/members/donations/campaigns")
    public ResponseDTO getCampaignsByDonator(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); String
		userName = userDetails.getUsername();
        log.info("CALL API SVMemberController.getCampaignsByDonator {userName: " + userName +"}");
        return new ResponseDTO.Builder().data(campaignService.getCampaignsByDonator(userName)).build();
    }
}