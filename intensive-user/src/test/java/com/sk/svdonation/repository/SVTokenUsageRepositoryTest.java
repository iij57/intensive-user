package com.sk.svdonation.repository;

import java.time.LocalDateTime;

import com.sk.svdonation.entity.SVBeneficiaryEntity;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVCampaignStatus;
import com.sk.svdonation.entity.SVDonationEntity;
import com.sk.svdonation.entity.SVMemberFoundationEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;
import com.sk.svdonation.entity.SVMemberType;
import com.sk.svdonation.entity.SVTokenUsageEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SVTokenUsageRepositoryTest {
    @Autowired
    SVTokenUsageRepository svTokenUsageRepository;
    @Autowired
    SVBeneficiaryRepository svBeneficiaryRepository;
    @Autowired
    SVCampaignRepository svCampaignRepository;
    @Autowired
    SVMemberRepository svUserRepository;
    @Autowired
    SVDonationRepository svDonationRepository;

    private SVCampaignEntity campaign;
    private long campaignId = 0l;
    private String campaignTitle = "title";
    private String campaignContent = "content";
    private String campaignImageUrl = "imageUrl";
    private LocalDateTime campaignStartAt = LocalDateTime.now();
    private LocalDateTime campaignFinishAt = LocalDateTime.now().plusDays(2);
    private long targetTokenAmount = 1_000_000;
    private SVCampaignStatus status = SVCampaignStatus.READY;
    private SVMemberFoundationEntity foundation;

    private SVDonationEntity donationEntity1;
    private SVDonationEntity donationEntity2;
    private SVDonationEntity donationEntity3;
    
    private SVBeneficiaryEntity beneficiaryEntity;

    private SVTokenUsageEntity tokenUsageEntity1;
    private SVTokenUsageEntity tokenUsageEntity2;
    private SVTokenUsageEntity tokenUsageEntity3;

    private SVMemberGeneralEntity donator;
    private SVMemberGeneralEntity beneficiary;

    
    String password = "test";
    String walletAddress = "walletAddress";
    String privateKey = "privateKey";

    String foundationMemberId = "foundation";
    SVMemberType foundationMemberType = SVMemberType.FOUNDATION;
    String foundationName = "행복재단";

    String donatorMemberId = "donator";
    SVMemberType donatorMemberType = SVMemberType.DONATOR;
    String memberName = "기부자";

    String beneficiaryMemberId = "beneficiary";
    SVMemberType beneficiaryMemberType = SVMemberType.BENEFICIARY;
    String beneficiaryName = "수혜자";

    @Before
    public void setup() {
        foundation = SVMemberFoundationEntity.create(foundationMemberId, password, foundationMemberType, walletAddress, privateKey, foundationName);
        svUserRepository.save(foundation);
        donator = SVMemberGeneralEntity.create(donatorMemberId, password, donatorMemberType, walletAddress, privateKey, memberName);
        svUserRepository.save(donator);
        beneficiary = SVMemberGeneralEntity.create(beneficiaryMemberId, password, beneficiaryMemberType, walletAddress, privateKey, beneficiaryName);
        svUserRepository.save(beneficiary);

        campaign = SVCampaignEntity.create("", campaignTitle, campaignContent, campaignImageUrl, campaignStartAt, campaignFinishAt, targetTokenAmount, status, foundation);
        svCampaignRepository.save(campaign);
        campaignId = campaign.getCampaignId();

        donationEntity1 = SVDonationEntity.create(1000, donator, campaign);
        svDonationRepository.save(donationEntity1);
        donationEntity2 = SVDonationEntity.create(2000, donator, campaign);
        svDonationRepository.save(donationEntity2);
        donationEntity3 = SVDonationEntity.create(3000, donator, campaign);
        svDonationRepository.save(donationEntity3);

        beneficiaryEntity = SVBeneficiaryEntity.create(2000, beneficiary, campaign);
        svBeneficiaryRepository.save(beneficiaryEntity);

        tokenUsageEntity1 =  SVTokenUsageEntity.create(1000, "스타벅스", beneficiaryEntity);
        svTokenUsageRepository.save(tokenUsageEntity1);
        tokenUsageEntity2 =  SVTokenUsageEntity.create(1000, "스타벅스", beneficiaryEntity);
        svTokenUsageRepository.save(tokenUsageEntity2);
        tokenUsageEntity3 =  SVTokenUsageEntity.create(1000, "스타벅스", beneficiaryEntity);
        svTokenUsageRepository.save(tokenUsageEntity3);
    }

    @After
    public void tearDown() {
        svTokenUsageRepository.delete(tokenUsageEntity1);
        svTokenUsageRepository.delete(tokenUsageEntity2);
        svTokenUsageRepository.delete(tokenUsageEntity3);

        svBeneficiaryRepository.delete(beneficiaryEntity);
        svDonationRepository.delete(donationEntity1);
        svDonationRepository.delete(donationEntity2);
        svDonationRepository.delete(donationEntity3);
        svCampaignRepository.deleteById(campaignId);
        svUserRepository.delete(foundation);
        svUserRepository.delete(donator);
    }

    @Test
    public void findAllByBeneficiaryTest() {
        svTokenUsageRepository.findAllByBeneficiary(beneficiaryEntity).forEach(usage -> {
            System.out.println("usage: " + usage.getPayment() + ", usage.beneficiaryToken: " + usage.getBeneficiary().getBeneficiaryToken() + ", usage.beneficiaryWallet:" + usage.getBeneficiary().getBeneficiaryMember().getWalletAddress());
        });
    }

}