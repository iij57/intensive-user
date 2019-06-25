package com.sk.svdonation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sk.svdonation.dto.SVBeneficiaryResponseDTO;
import com.sk.svdonation.dto.SVCampaignResponseDTO;
import com.sk.svdonation.dto.SVWalletHistoryDTO;
import com.sk.svdonation.repository.SVBeneficiaryRepository;
import com.sk.svdonation.service.SVBeneficiaryService;
import com.sk.svdonation.service.SVCampaignService;
import com.sk.svdonation.service.SVWalletService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BroadCastConfig {
	
	@Autowired
	private SimpMessagingTemplate brokerMessagingTemplate;
	
	@Autowired
	private SVCampaignService campaignService;
	
	@Autowired
	private SVBeneficiaryService beneficiaryService;
	
	@Autowired
	private SVBeneficiaryRepository beneficiaryRepository;
	
	@Autowired
	private SVWalletService walletService;
	
	//@Scheduled(fixedDelay = 2000)
	public void sendCampaigns() {
		
		log.info("send socket BoradCastConfig.sendCampaigns()");
		
		List<SVCampaignResponseDTO> returnValue = campaignService.getCampaigns();
		
		
		this.brokerMessagingTemplate.convertAndSend("/topic/campaigns", returnValue);
	}
	
	//@Scheduled(fixedDelay = 5000)
	public void sendBeneficiary() {
		
		log.info("send socket BoradCastConfig.sendBeneficiary()");
		HashMap<String, Object> returnValue = new HashMap<>();
		
		ArrayList <HashMap<String,Object>> totalList = new ArrayList<HashMap<String,Object>>();

		
		//1번 캠페인에 대한 카테고리별 사용 내역 총합 가져오기
		Iterable<Object[]> totals =  beneficiaryRepository.findAllByCategory();
		for(Object[] total : totals) {
			HashMap<String, Object> totalMap = new HashMap<>();
			totalMap.put(total[0].toString(), total[1]);
			totalList.add(totalMap);
		}
		returnValue.put("chart", totalList);
		
		//현재 수혜자는 1번 캠페인에서만
		ArrayList <HashMap<String,Object>> historyList = new ArrayList<HashMap<String,Object>>();
		List<SVBeneficiaryResponseDTO> beneficiary = beneficiaryService.getBeneficiaries(1);
		
		for(SVBeneficiaryResponseDTO member : beneficiary) {
			
			HashMap<String, Object> historyMap = new HashMap<>();
			
			List<SVWalletHistoryDTO> history = walletService.getSVCHistory(member.getBeneficiaryMember().getMemberId());
			
			historyMap.put(member.getBeneficiaryMember().getName(), history);
			
			historyList.add(historyMap);
			
		}
		
		returnValue.put("list", historyList);
		
		this.brokerMessagingTemplate.convertAndSend("/topic/beneficiary", returnValue);
	}
	
}
