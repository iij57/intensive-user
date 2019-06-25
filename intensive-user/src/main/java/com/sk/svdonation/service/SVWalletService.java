package com.sk.svdonation.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sk.svdonation.dto.SVPaymentDTO;
import com.sk.svdonation.dto.SVWalletHistoryDTO;
import com.sk.svdonation.entity.SVBeneficiaryEntity;
import com.sk.svdonation.entity.SVTokenUsageEntity;
import com.sk.svdonation.repository.SVTokenUsageRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SVWalletService {
    private SVDonationService donationService;
    private SVVotingService votingService;
    private SVGoodbuyService goodbuyService;
    
    @Autowired
    private SVBeneficiaryService beneficiaryService;
    	
    @Autowired
    private SVTokenUsageRepository tokenUsageRepository;
    
    @Autowired
    private SVWeb3JService web3jService;

    private enum CATEGORY {
        DONATION, GOODBUY, VOTE, TOKEN
    }
    private Comparator<SVWalletHistoryDTO> comparator = (o1, o2) -> {
        if(o1.getLdt_usedAt().isAfter(o2.getLdt_usedAt())) {
            return -1;
        } else if(o1.getLdt_usedAt().isBefore(o2.getLdt_usedAt())) {
            return 1;
        }
        return 0;
    };

    public SVWalletService(SVDonationService donationService, SVVotingService votingService, SVGoodbuyService goodbuyService) {
        this.donationService = donationService;
        this.votingService = votingService;
        this.goodbuyService = goodbuyService;
    }

    /**
     * 사용자의 SVC 사용 이력을 조회하는 서비스
     * 사용처 : 기부, 착한소비
     * @param memberId
     * @return 사용내역
     */
    public List<SVWalletHistoryDTO> getSVCHistory(String memberId) {
        log.info("CALL SVWalletService.getSVCHistory {memberId: " + memberId + "}");
        List<SVWalletHistoryDTO> returnValue = new ArrayList<>();
        donationService.getDonationEntities(memberId).forEach(donation -> {
            // returnValue.add(new SVWalletHistoryDTO(donation.getAmountOfSVC(), donation.getAmountOfSVP(), CATEGORY.DONATION.toString(), donation.getCampaign().getCampaignTitle(), donation.getCampaign().getStatus().toString()));
            String usedAt = donation.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            returnValue.add(new SVWalletHistoryDTO(donation.getAmountOfSVC(), 0l, CATEGORY.DONATION.toString(), donation.getCampaign().getCampaignTitle(), donation.getCampaign().getStatus().toString(), donation.getCreateAt(), usedAt));
        });
        goodbuyService.getGoodbuyEntitiesByMember(memberId).forEach(buy -> {
            String usedAt = buy.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            returnValue.add(new SVWalletHistoryDTO(buy.getSvcAmount(), buy.getSvpAmount(), CATEGORY.GOODBUY.toString(), buy.getProductName(), "", buy.getCreateAt(), usedAt));
        });
        getPaymentByMemberId(memberId).forEach(token -> {
        	String usedAt = token.getUsageAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        	returnValue.add(new SVWalletHistoryDTO(token.getPayment(), 0l, CATEGORY.TOKEN.toString(), token.getCategory(), "", token.getUsageAt(), usedAt));
        });
        

        Collections.sort(returnValue, comparator);
        return returnValue;
    }

    /**
     * 사용자의 SVP 사용이력을 조회하는 서비스
     * 사용처: 착한소비, 투표
     * @param memberId
     */
    public List<SVWalletHistoryDTO> getSVPHistory(String memberId) {
        log.info("CALL SVWalletService.getSVPHistory {memberId: " + memberId + "}");
        List<SVWalletHistoryDTO> returnValue = new ArrayList<>();
        goodbuyService.getGoodbuyEntitiesByMember(memberId).forEach(buy -> {
            String usedAt = buy.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            returnValue.add(new SVWalletHistoryDTO(buy.getSvcAmount(), buy.getSvpAmount(), CATEGORY.GOODBUY.toString(), buy.getProductName(), "", buy.getCreateAt(), usedAt));
        });
        votingService.getVotingEntitiesByMemberId(memberId).forEach(vote -> {
            String usedAt = vote.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            returnValue.add(new SVWalletHistoryDTO(0l, vote.getSvpAmount(), CATEGORY.VOTE.toString(), vote.getCampaign().getCampaignTitle(), "", vote.getCreateAt(), usedAt));
        });
        
        LocalDateTime targetDateTime = LocalDateTime.of(2019, 05, 20, 9, 00, 00, 00);

        returnValue.add(new SVWalletHistoryDTO(0l, 55l, "REWARD", "혼자 엄마, 당당한 엄마" , "", targetDateTime, "2019-05-20 09:00:00.000"));

        Collections.sort(returnValue, comparator);
        
        return returnValue;
    }
    
    /**
     * 모바일 월렛 에서 QR로 구매 하는 서비스
     * @param memberId, payment
     * @return 구매성공 여부
     */
    public boolean create(SVPaymentDTO payment, String memberId) {
    	log.info("CALL SVWalletService.create {payment: " + payment +"}, memberId: " + memberId);
    	
    	//수혜자가 받은 어느 캠페인에서 사용 되는지 확인을 해야하는데....
    	//처음에서 계속 까내려 가야하지만... 일단은...보진 않으니 처음 수혜 받은 캠페인에서 가져 가도록함
    	
    	List<SVBeneficiaryEntity> beneficiary = beneficiaryService.getBeneficiariesByMemberId(memberId);
    	
    	SVTokenUsageEntity entity = SVTokenUsageEntity.create(payment.getPayment(), payment.getCategory(), beneficiary.get(0));
    	
    	tokenUsageRepository.save(entity);	
    	
    	try {
    		web3jService.sendRawTransactionAndReturnReceipt(payment.getSignedTx());
		} catch (Exception e) {
    		log.error("Payment Failed!!: ", e);
			// TODO: handle exception
    		throw new RuntimeException(e);
		}
    	
    	return entity.getTokenUsageId() > 0 ? true : false;
    	
    	
    }
    
    /**
     * 모바일 월렛 에서 QR로 구매 한 내역을 가져 오는 서비스
     * @param memberId
     * @return 구매내역
     */
    public List<SVTokenUsageEntity> getPaymentByMemberId(String memberId){
    	log.info("CALL SVWalletService.getPaymentByMemberId {memberID: " + memberId + "}");
    	
    	List<SVBeneficiaryEntity> beneficiary = beneficiaryService.getBeneficiariesByMemberId(memberId);
    	
    	List<SVTokenUsageEntity> returnValue = new ArrayList<>();
    	if(beneficiary.size() != 0) {
    		tokenUsageRepository.findAllByBeneficiary(beneficiary.get(0)).forEach(tokenUsage ->{
        		returnValue.add(tokenUsage);
        	});;	
    	}
    	
    	log.info("RETURN SVWalletService.getPaymentByMemberId {returnValue: " + returnValue + "}");
    	
    	return returnValue;
    	
    }
}