package com.sk.svdonation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.service.SVStatisticsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public  class SVStatisticsController {
	
	@Autowired
	private SVStatisticsService statisticsService;
    
    @GetMapping("/v1/statistics")
    public ResponseDTO getStatisticsByMember(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        log.info("CALL API SVStatisticsController.getStatisticsByMember {member: " + userName + "}");
        
        return new ResponseDTO.Builder().data(statisticsService.getStatisticsByMember(userName)).build();
    }
    
}