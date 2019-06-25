package com.sk.svdonation.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sk.svdonation.dto.SVEthTxDTO;
import com.sk.svdonation.dto.SVVotingDTO;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVMemberBaseEntity;
import com.sk.svdonation.entity.SVVotingEntity;
import com.sk.svdonation.repository.SVVotingRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class SVVotingService {
    private SVVotingRepository votingRepository;
    private SVMemberService memberService;
    private SVWeb3JService web3jService;
    
    public List<SVVotingEntity> getVotingByCampaignId(long campaignId) {
        log.info("CALL SVVotingService.getVotingByCampaignId {campaignId: " + campaignId + "}");
        List<SVVotingEntity> returnValue = votingRepository.findByCampaign(SVCampaignEntity.create(campaignId));
        
        log.info("RETURN SVVotingService.getVotingByCampaignId {returnValue: " + returnValue + "}");
        return returnValue;
    }

    List<SVVotingEntity> getVotingEntitiesByMemberId(String memberId) {
        log.info("CALL SVVotingService.getVotingEntitiesByMemberId {memberId: " + memberId + "}");
        List<SVVotingEntity> returnValue = votingRepository.findAllByMemberId(memberId);
        log.info("RETURN SVVotingService.getVotingEntitiesByMemberId {returnValue: " + returnValue + "}");
        return returnValue;
    }
    
    
    /**
     * 기부자가 SVP를 이용하여 투표하는 로직
     * @param vote 정보 {@link SVVotingDTO}
     * @return 투표 성공 여부
     */
    public boolean create(SVVotingDTO voting, String userName) {
    	log.info("CALL SVVotingService.create {vote: " + voting +"}");
    	
    	SVMemberBaseEntity memberEntity = memberService.getSVMemberEntity(userName);
        long accountBalance = web3jService.getSVPBalance(memberEntity.getWalletAddress());
        if(accountBalance < voting.getSvpAmount()) throw new IllegalArgumentException("Not enough account balance [" + accountBalance + "]. required voting amount [" + voting.getSvpAmount() + "]");
    	
		SVVotingEntity entity = convert(voting, userName);
		votingRepository.save(entity);
    	
    	try {

            // // token approve transaction
            // web3jService.sendRawTransactionAndReturnReceipt(voting.getApproveSignedTx());
            // // donate transaction
            // web3jService.sendRawTransactionAndReturnReceipt(voting.getSignedTx());
            
            SVEthTxDTO txData = SVEthTxDTO.builder().signedTx(voting.getApproveSignedTx()).successCallBack(message -> {
                web3jService.sendRawTransactionAndReturnReceipt(voting.getSignedTx());
            }).errorCallBack(message -> {
                log.error("Voting failed: ", message);
            }).build();

            web3jService.sendRawTransactionAsync(txData);
    	}catch (Exception e) {
    		log.error("Voting Failed!!: ", e);
			// TODO: handle exception
    		throw new RuntimeException(e);
		}
    	
    	return entity.getVoteId() > 0 ? true : false;
    	
    }
    
    private SVVotingEntity convert(SVVotingDTO voting, String userName) {
        return SVVotingEntity.create(userName, voting.getCampaignId(), voting.getSvpAmount());

    }

}