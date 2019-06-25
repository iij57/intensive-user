package com.sk.svdonation.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.svdonation.dto.SVStatisticsDTO;
import com.sk.svdonation.repository.SVStatisticsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SVStatisticsService {
	
	@Autowired
	private SVStatisticsRepository statisticsRepository;

	public SVStatisticsDTO getStatisticsByMember(String memberId) {
        log.info("CALL SVStatisticsService.getStatisticsByMember {memberId: " + memberId + "}");

        List<Object[]> campaignsList = statisticsRepository.findDonateCampaignsByMember(memberId);
        
        List<Object[]> svcList = statisticsRepository.findDonateSVCByMember(memberId);
        Object svcValue = svcList.get(0);
        
        List<Object[]> svpList = statisticsRepository.findVotingSVPByMember(memberId);
        Object svpValue = svpList.get(0);
        
        SVStatisticsDTO returnValue = new SVStatisticsDTO( Long.valueOf(campaignsList.size()), Long.valueOf(svcValue.toString()), Long.valueOf(svpValue.toString()));
        
        return returnValue;
    }
	

}