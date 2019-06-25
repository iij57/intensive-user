package com.sk.svdonation.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sk.svdonation.dto.SVBeneficiaryPostDTO;
import com.sk.svdonation.dto.SVBeneficiaryResponseDTO;
import com.sk.svdonation.dto.SVBeneficiaryUsageDTO;
import com.sk.svdonation.dto.SVMemberDTO;
import com.sk.svdonation.entity.SVBeneficiaryEntity;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;
import com.sk.svdonation.repository.SVBeneficiaryRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class SVBeneficiaryService {
    private static final Logger logger = LoggerFactory.getLogger(SVBeneficiaryService.class);

    private SVBeneficiaryRepository beneficiaryRepository;
    private SVCampaignService campaignService;
    private SVWeb3JService web3jService;

    public List<SVBeneficiaryResponseDTO> getBeneficiaries(long campaignId) {
        logger.info("CALL SVBeneficiaryService.getBeneficiaries {campaignId: " + campaignId + "}");
        final List<SVBeneficiaryResponseDTO> returnValue = new ArrayList<>();

        beneficiaryRepository.findAllByCampaign(SVCampaignEntity.create(campaignId)).forEach(beneficiary -> {
            returnValue.add(convert(beneficiary));
        });

        return returnValue;
    }
    
    public List<SVBeneficiaryUsageDTO> getBeneficiariesTokenUsageByCampaignId(long campaignId) {
        logger.info("CALL SVBeneficiaryService.getBeneficiariesTokenUsageByCampaignId {campaignId: " + campaignId + "}");
        final List<SVBeneficiaryUsageDTO> returnValue = new ArrayList<>();
        
        Iterable<Object[]> usageList = beneficiaryRepository.findAllByTokenUsageByCampaign(campaignId);
        
        for(Object[] usageNative : usageList) {
        	
        	SVBeneficiaryUsageDTO usage = new SVBeneficiaryUsageDTO(usageNative[6].toString(), usageNative[5].toString(), Long.parseLong(usageNative[4].toString()), Long.parseLong(usageNative[0].toString()), Long.parseLong(usageNative[3].toString()), usageNative[2].toString(), usageNative[1].toString());
        	
        	returnValue.add(usage);
        }


        return returnValue;
    }
    
    public List<SVBeneficiaryEntity> getBeneficiariesByMemberId(String memberId) {
        logger.info("CALL SVBeneficiaryService.getBeneficiariesByMemberId {memberId: " + memberId + "}");
        final List<SVBeneficiaryEntity> returnValue = new ArrayList<>();
        
        returnValue.addAll(beneficiaryRepository.findAllByBeneficiaryMember(SVMemberGeneralEntity.create(memberId)));
        
        return returnValue;
    }

    /**
     * 수혜자에게 토큰을 지급하는 로직
     * @param beneficiary 지급정보
     * @param memberId 수혜자 아이디
     * @return 지급결과
     * @throws IllegalArgumentException campaign 컨트랙트의 balance가 수혜자에게 지급해야하는 급액보다 작은 경우
     */ 
    public boolean addBeneficiary(SVBeneficiaryPostDTO beneficiary, String memberId) {
        logger.info("CALL SVBeneficiaryService.addBeneficiary {beneficiary: " + beneficiary + "}");

        // campaign 컨트랙트의 balance가 수혜자에게 지급해야하는 급액보다 작은 경우 exception
        SVCampaignEntity campaignEntity = campaignService.getCampaignEntity(beneficiary.getCampaignId());
        long campaignBalance = web3jService.getSVCBalance(campaignEntity.getCampaignAddress());
        if(campaignBalance < beneficiary.getBeneficiaryToken()) throw new IllegalArgumentException("Not enough campaign balance [" + campaignBalance + "]. required beneficiary amount [" + beneficiary.getBeneficiaryToken() + "]");

        final SVBeneficiaryEntity entity = convert(beneficiary, memberId);
        beneficiaryRepository.save(entity);

        try {
            web3jService.sendRawTransactionAndReturnReceipt(beneficiary.getSignedTx());
        } catch (Exception e) {
            logger.error("Add beneficiary failed!!: ", e);
            // TODO Exception 정의 필요
            throw new RuntimeException(e);
        }

        return entity.getBeneficiaryId() > 0 ? true : false;
    }

    private SVBeneficiaryResponseDTO convert(SVBeneficiaryEntity beneficiary) {
        SVMemberGeneralEntity beneficiaryMember = beneficiary.getBeneficiaryMember();
        SVMemberDTO member = new SVMemberDTO(beneficiaryMember.getMemberId(), beneficiaryMember.getMemeberName(), beneficiaryMember.getMemberType().toString(), beneficiaryMember.getWalletAddress(), beneficiaryMember.getPrivateKey());
        
        return new SVBeneficiaryResponseDTO(
                                    String.valueOf(beneficiary.getBeneficiaryId()), 
                                    beneficiary.getBeneficiaryToken(), 
                                    beneficiary.getCampaign().getCampaignId(), 
                                    member
                                    );
    }

    private SVBeneficiaryEntity convert(SVBeneficiaryPostDTO beneficiary, String memberId) {
        return SVBeneficiaryEntity.create(
                                            beneficiary.getBeneficiaryToken(), 
                                            SVMemberGeneralEntity.create(memberId), 
                                            SVCampaignEntity.create(beneficiary.getCampaignId())
                                        );
    }
}