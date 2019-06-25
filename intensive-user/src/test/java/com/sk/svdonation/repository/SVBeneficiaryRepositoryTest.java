package com.sk.svdonation.repository;

import java.time.LocalDateTime;

import com.sk.svdonation.entity.SVBeneficiaryEntity;
import com.sk.svdonation.entity.SVCampaignEntity;
import com.sk.svdonation.entity.SVCampaignStatus;
import com.sk.svdonation.entity.SVDonationEntity;
import com.sk.svdonation.entity.SVMemberFoundationEntity;
import com.sk.svdonation.entity.SVMemberGeneralEntity;
import com.sk.svdonation.entity.SVMemberType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SVBeneficiaryRepositoryTest {
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
    private SVBeneficiaryEntity beneficiaryEntity1;
    private SVBeneficiaryEntity beneficiaryEntity2;
    private SVBeneficiaryEntity beneficiaryEntity3;
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

        beneficiaryEntity1 = SVBeneficiaryEntity.create(2000, beneficiary, campaign);
        svBeneficiaryRepository.save(beneficiaryEntity1);
        beneficiaryEntity2 = SVBeneficiaryEntity.create(2000, beneficiary, campaign);
        svBeneficiaryRepository.save(beneficiaryEntity2);
        beneficiaryEntity3 = SVBeneficiaryEntity.create(2000, beneficiary, campaign);
        svBeneficiaryRepository.save(beneficiaryEntity3);
    }

    @After
    public void tearDown() {
        svBeneficiaryRepository.delete(beneficiaryEntity1);
        svBeneficiaryRepository.delete(beneficiaryEntity2);
        svBeneficiaryRepository.delete(beneficiaryEntity3);
        svDonationRepository.delete(donationEntity1);
        svDonationRepository.delete(donationEntity2);
        svDonationRepository.delete(donationEntity3);
        svCampaignRepository.deleteById(campaignId);
        svUserRepository.delete(foundation);
        svUserRepository.delete(donator);
        svUserRepository.delete(beneficiary);
    }

    @Test
    public void findAllByCampaignTest() {
        svBeneficiaryRepository.findAllByCampaign(campaign).forEach(beneficiary ->  {
            System.out.println("beneficiary: " + beneficiary.getBeneficiaryToken() + ", beneficaryName: " + beneficiary.getBeneficiaryMember().getWalletAddress());
        });
    }
}