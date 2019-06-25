package com.sk.svdonation.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.sk.svdonation.dto.SVBeneficiaryResponseDTO;
import com.sk.svdonation.dto.SVPaymentDTO;
import com.sk.svdonation.dto.SVWalletHistoryDTO;
import com.sk.svdonation.repository.SVBeneficiaryRepository;
import com.sk.svdonation.service.SVBeneficiaryService;
import com.sk.svdonation.service.SVWalletService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
public  class SVWalletController {
    private SVWalletService walletService;
    
    @Autowired
    private BroadCastConfig broadcast;
    
    @Autowired
    private SVBeneficiaryRepository beneficiaryRepository; 
    
    @Autowired
    private SVBeneficiaryService beneficiaryService;
    
    @GetMapping("/v1/wallet/svchistory")
    public ResponseDTO getMyWalletSVCHistory(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        log.info("CALL API SVWalletController.getMyWalletSVCHistory {member: " + userName + "}");
        return new ResponseDTO.Builder().data(walletService.getSVCHistory(userName)).build();
    }
    
    @GetMapping("/v1/wallet/campaignsvchistory")
    public ResponseDTO getMyWalletCampaignSVCHistory() {
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
		
		return new ResponseDTO.Builder().data(returnValue).build();
    }
    
    @GetMapping("/v1/wallet/{memberId}/svchistory")
    public ResponseDTO getMyWalletSVCHistoryByMemberId(@PathVariable String memberId) {
        log.info("CALL API SVWalletController.getMyWalletSVCHistoryByMemberId {member: " + memberId + "}");
        List<SVWalletHistoryDTO> histories = walletService.getSVCHistory(memberId);
        Map<String, Long> suminfo = new HashMap<>();
        histories.forEach(history -> {
            suminfo.compute(history.getName(), (name, sum) -> Optional.ofNullable(sum).map(v -> v += history.getAmountOfSVC()).orElse(history.getAmountOfSVC()));
        });

        Map<String, Object> returnData = new HashMap<>();
        returnData.put("histories", histories);
        returnData.put("suminfo", suminfo);

        return new ResponseDTO.Builder().data(returnData).build();
    }
    
    @GetMapping("/v1/wallet/svphistory")
    public ResponseDTO getMyWalletSVPHistory(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userName = userDetails.getUsername();
        log.info("CALL API SVWalletController.getMyWalletSVPHistory {member: " + userName + "}");
        return new ResponseDTO.Builder().data(walletService.getSVPHistory(userName)).build();
    }
    
    @PostMapping("/v1/wallet/{memberId}/payment")
    public ResponseDTO create(@RequestBody SVPaymentDTO payment, @PathVariable String memberId) {
        log.info("CALL API SVWalletController.create {payment :  " + payment + ", memberId: " + memberId + "}");
        boolean result = walletService.create(payment, memberId);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("payment", result);
        
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
        Date time = new Date();
        String time1 = format.format(time);
        
        responseData.put("payment", result);
        responseData.put("returnTime", time1);
        
        //수혜자 구매 완료시 소켓 통신
        broadcast.sendBeneficiary();
        
        return new ResponseDTO.Builder().data(responseData).build();
    }
}