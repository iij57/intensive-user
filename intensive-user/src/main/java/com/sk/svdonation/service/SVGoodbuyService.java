package com.sk.svdonation.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.sk.svdonation.dto.SVEthTxDTO;
import com.sk.svdonation.dto.SVGoodbuyDTO;
import com.sk.svdonation.dto.SVGoodbuyResponseDTO;
import com.sk.svdonation.entity.SVGoodbuyEntity;
import com.sk.svdonation.entity.SVGoodbuyStatus;
import com.sk.svdonation.entity.SVMemberBaseEntity;
import com.sk.svdonation.repository.SVGoodbuyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class SVGoodbuyService {
    private SVGoodbuyRepository buyRepository;
    private SVMemberService memberService;
    private SVWeb3JService web3jService;
    
    @Autowired
    private SVGoodbuyRepository svGoodbuyRepository;
    
    /**
     * 기부자가 SVC, SVP를 이용한 착한 소비
     * @param 구매 정보 {@link SVGoodBuy}
     * @return 구매성공여부
     */
    public long create(SVGoodbuyDTO buy, String userName) {
    	log.info("CALL SVGoodBuyService.create {goodbuy: " + buy +"}");
    	
    	SVMemberBaseEntity memberEntity = memberService.getSVMemberEntity(userName);
        long accountBalance1 = web3jService.getSVCBalance(memberEntity.getWalletAddress());
        long accountBalance2= web3jService.getSVPBalance(memberEntity.getWalletAddress());
        
        if(accountBalance1 < buy.getSvcAmount()) throw new IllegalArgumentException("Not enough account balance [" + accountBalance1 + "]. required buy svc amount [" + buy.getSvcAmount() + "]");
        if(accountBalance2 < buy.getSvpAmount()) throw new IllegalArgumentException("Not enough account balance [" + accountBalance2 + "]. required buy svp amount [" + buy.getSvpAmount() + "]");
        
    	
        SVGoodbuyEntity entity = convert(buy, userName);
		buyRepository.save(entity);
    	
    	try {

            //web3jService.sendRawTransactionAndReturnReceipt(buy.getSvcAproveSignedTx());
            // web3jService.sendRawTransactionAndReturnReceipt(buy.getSvcSignedTx());
            SVEthTxDTO txData1 = SVEthTxDTO.builder().signedTx(buy.getSvcSignedTx()).successCallBack(message -> {
                log.info("Donate success: ", message);
            }).errorCallBack(message -> {
                log.error("Donate failed: ", message);
            }).build();
            web3jService.sendRawTransactionAsync(txData1);
            
            //web3jService.sendRawTransactionAndReturnReceipt(buy.getSvpApproveSignedTx());
            // web3jService.sendRawTransactionAndReturnReceipt(buy.getSvpSignedTx());
            SVEthTxDTO txData2 = SVEthTxDTO.builder().signedTx(buy.getSvpSignedTx()).successCallBack(message -> {
                log.info("Donate success: ", message);
            }).errorCallBack(message -> {
                log.error("Donate failed: ", message);
            }).build();
            web3jService.sendRawTransactionAsync(txData2);
    		
    	}catch (Exception e) {
    		log.error("GoodBuy Failed!!: ", e);
			// TODO: handle exception
    		throw new RuntimeException(e);
		}
    	
    	return entity.getBuyId();
    	
    }
    
    /**
     * 기부자가 구매한 착한소비 내역을 리턴
     * @param 구매자
     * @return 구매내역
     */
    public List<SVGoodbuyResponseDTO> getGoodbuyByMember(String memberId) {
        log.info("CALL SVGoodbuyService.getGoodbuyByMember {memberId: " + memberId + "}");
        List<SVGoodbuyResponseDTO> returnValue = new ArrayList<>();
        
        buyRepository.findByMemberIdOrderByCreateAtDesc(memberId).forEach(buy -> {
            returnValue.add(dateConvert(buy));
        });
        
        log.info("RETURN SVGoodbuyService.getGoodbuyByMember {returnValue: " + returnValue + "}");
        return returnValue;
    }
    
    public SVGoodbuyResponseDTO getGoodbuyByBuyId(long buyId) {
        log.info("CALL SVGoodbuyService.getGoodbuyByBuyId {buyId: " + buyId + "}");
        SVGoodbuyEntity goodbuyEntity = svGoodbuyRepository.findByBuyId(buyId);
        
        SVGoodbuyResponseDTO returnValue = new SVGoodbuyResponseDTO(goodbuyEntity.getBuyId(), goodbuyEntity.getProductName(), goodbuyEntity.getMemberId(), goodbuyEntity.getSvcAmount(), goodbuyEntity.getSvpAmount(), goodbuyEntity.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), goodbuyEntity.getProductStatus());
        
        log.info("RETURN SVGoodbuyService.getGoodbuyByBuyId {returnValue: " + returnValue + "}");
        return returnValue;
    }

    Iterable<SVGoodbuyEntity> getGoodbuyEntitiesByMember(String memberId) {
        log.info("CALL SVGoodbuyService.getGoodbuyEntitiesByMember {memberId: " + memberId + "}");
        log.info("RETURN SVGoodbuyService.getGoodbuyByMember");
        return buyRepository.findByMemberIdOrderByCreateAtDesc(memberId);
    }
    
    private SVGoodbuyEntity convert(SVGoodbuyDTO buy, String userName) {
        return SVGoodbuyEntity.create(userName, buy.getProductName(), buy.getSvcAmount(), buy.getSvpAmount(),SVGoodbuyStatus.결제완료);
    }
    
    private SVGoodbuyResponseDTO dateConvert(SVGoodbuyEntity buy) {
    	return new SVGoodbuyResponseDTO(buy.getBuyId(), buy.getProductName(), buy.getMemberId(), buy.getSvcAmount(), buy.getSvpAmount(), buy.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), buy.getProductStatus());
    }
    
    

}