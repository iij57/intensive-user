package com.sk.svdonation.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.sk.svdonation.dto.SVDonationPostDTO;
import com.sk.svdonation.dto.SVDonationResponseDTO;
import com.sk.svdonation.dto.SVEthTxDTO;
import com.sk.svdonation.dto.SVMemberDTO;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVCampaignStatus;
import com.sk.svdonation.entity.SVDonationEntity;
import com.sk.svdonation.entity.SVMemberBaseEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;
import com.sk.svdonation.repository.SVDonationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SVDonationService {
    private static final Logger logger = LoggerFactory.getLogger(SVDonationService.class);

    private SVDonationRepository donationRepository;
    private SVMemberService memberService;
    private SVWeb3JService web3jService;

    public List<SVDonationResponseDTO> getDonations(long campaignId) {
        logger.info("CALL SVDonationService.getDonations {campaignId: " + campaignId + "}");
        final List<SVDonationResponseDTO> returnValue = new ArrayList<>();
        donationRepository.findAllByCampaign(SVCampaignEntity.create(campaignId)).forEach(donation -> {
            returnValue.add(convert(donation));
        });
        logger.info("RETURN SVDonationService.getDonations {returnValue: " + returnValue + "}");
        return returnValue;
    }

    public List<SVDonationResponseDTO> getDonations(String memberId) {
        List<SVDonationResponseDTO> returnValue = new ArrayList<>();
        donationRepository.findAllByDonator(SVMemberGeneralEntity.create(memberId)).forEach(donation -> {
            returnValue.add(convert(donation));
        });

        return returnValue;
    }

    public List<SVDonationResponseDTO> getDonationReady(String memberId) {
        List<SVDonationResponseDTO> returnValue = new ArrayList<>();
        donationRepository.findAllByDonator(SVMemberGeneralEntity.create(memberId)).forEach(donation -> {
            // 상태가 ING 인것은 현재 기부 진행중인 캠페인이므로 기부 대기 상태이며 캠페인 모금 성공시 기부 완료가 된다.
            if(donation.getCampaign().getStatus() == SVCampaignStatus.ING) {
                returnValue.add(convert(donation));
            }
        });

        return returnValue;
    }

    Iterable<SVDonationEntity> getDonationEntities(String memberId) {
        return donationRepository.findAllByDonator(SVMemberGeneralEntity.create(memberId));
    }

    /**
     * 기부자가 토큰을 기부하는 로직
     * @param donation 기부 정보 {@link SVDonationResponseDTO}
     * @param donator 기부자 아이디
     * @return 기부 성공 여부
     * @throws IllegalArgumentException campaign 컨트랙트의 balance가 수혜자에게 지급해야하는 급액보다 작은 경우
     */
    @Transactional
    public boolean donate(SVDonationPostDTO donation, String donator) {
        logger.info("CALL SVDonationService.donate {donation: " + donation + ", donator: " + donator + "}");
        SVMemberBaseEntity memberEntity = memberService.getSVMemberEntity(donator);
        long accountBalance = web3jService.getSVCBalance(memberEntity.getWalletAddress());
        if(accountBalance < donation.getAmountOfSVC()) throw new IllegalArgumentException("Not enough account balance [" + accountBalance + "]. required donate amount [" + donation.getAmountOfSVC() + "]");

        SVDonationEntity entity = convert(donation, donator);
        donationRepository.save(entity);

        try {
            // // token approve transaction
            // web3jService.sendRawTransactionAndReturnReceipt(donation.getApproveSignedTx());
            // // donate transaction
            // web3jService.sendRawTransactionAndReturnReceipt(donation.getSignedTx());
            SVEthTxDTO txData = SVEthTxDTO.builder().signedTx(donation.getApproveSignedTx()).successCallBack(message -> {
                web3jService.sendRawTransactionAndReturnReceipt(donation.getSignedTx());
            }).errorCallBack(message -> {
                logger.error("Donate failed: ", message);
            }).build();

            web3jService.sendRawTransactionAsync(txData);
        } catch (Exception e) {
            logger.error("Donate failed!!: ", e);
            // TODO Exception 정의 필요
            throw new RuntimeException(e);

        }

        return entity.getDonationId() > 0 ? true : false;
    }

    private SVDonationResponseDTO convert(SVDonationEntity donation) {
        SVMemberDTO donator = new SVMemberDTO(donation.getDonator().getMemberId(), donation.getDonator().getMemeberName(), donation.getDonator().getMemberType().toString(), donation.getDonator().getWalletAddress(), donation.getDonator().getPrivateKey());
        return new SVDonationResponseDTO(String.valueOf(donation.getDonationId()), donation.getAmountOfSVC(), donation.getCampaign().getCampaignId(), donator);
    }

    private SVDonationEntity convert(SVDonationPostDTO donation, String donator) {
        return SVDonationEntity.create(donation.getAmountOfSVC(), SVMemberGeneralEntity.create(donator), SVCampaignEntity.create(donation.getCampaignId()));
    }
}