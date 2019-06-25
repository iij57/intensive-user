package com.sk.svdonation.repository;

import com.sk.svdonation.entity.SVCampaignEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SVCampaignRepositoryTest {
    @Autowired
    SVCampaignRepository svCampaignRepository;
    // @Autowired
    // SVMemberRepository svUserRepository;
    // @Autowired
    // SVDonationRepository svDonationRepository;

    // private long campaignId = 0l;
    // private String campaignTitle = "title";
    // private String campaignContent = "content";
    // private String campaignImageUrl = "imageUrl";
    // private LocalDateTime campaignStartAt = LocalDateTime.now();
    // private LocalDateTime campaignFinishAt = LocalDateTime.now().plusDays(2);
    // private long targetTokenAmount = 1_000_000;
    // private SVCampaignStatus status = SVCampaignStatus.READY;
    // private SVMemberFoundationEntity foundation;

    // private SVDonationEntity donationEntity1;
    // private SVDonationEntity donationEntity2;
    // private SVDonationEntity donationEntity3;
    // private SVMemberGeneralEntity donator;

    
    // String password = "test";
    // String walletAddress = "walletAddress";
    // String privateKey = "privateKey";

    // String foundationMemberId = "foundation";
    // SVMemberType foundationMemberType = SVMemberType.FOUNDATION;
    // String foundationName = "행복재단";

    // String donatorMemberId = "donator";
    // SVMemberType donatorMemberType = SVMemberType.DONATOR;
    // String memberName = "기부자";



    // @Before
    // public void setup() {
    //     foundation = SVMemberFoundationEntity.create(foundationMemberId, password, foundationMemberType, walletAddress, privateKey, foundationName);
    //     svUserRepository.save(foundation);
    //     donator = SVMemberGeneralEntity.create(donatorMemberId, password, donatorMemberType, walletAddress, privateKey, memberName);
    //     svUserRepository.save(donator);

    //     SVCampaignEntity campaign = SVCampaignEntity.create("", campaignTitle, campaignContent, campaignImageUrl, campaignStartAt, campaignFinishAt, targetTokenAmount, status, foundation);
    //     svCampaignRepository.save(campaign);
    //     campaignId = campaign.getCampaignId();

    //     donationEntity1 = SVDonationEntity.create(1000, donator, campaign);
    //     svDonationRepository.save(donationEntity1);
    //     donationEntity2 = SVDonationEntity.create(2000, donator, campaign);
    //     svDonationRepository.save(donationEntity2);
    //     donationEntity3 = SVDonationEntity.create(3000, donator, campaign);
    //     svDonationRepository.save(donationEntity3);
    // }

    // @After
    // public void tearDown() {
    //     svDonationRepository.delete(donationEntity1);
    //     svDonationRepository.delete(donationEntity2);
    //     svDonationRepository.delete(donationEntity3);
    //     svCampaignRepository.deleteById(campaignId);
    //     svUserRepository.delete(foundation);
    //     svUserRepository.delete(donator);
    // }

    @Test
    public void findAllTest() {
        long start = System.currentTimeMillis();

        Iterable<SVCampaignEntity> list = svCampaignRepository.findAll();
        
        long end = System.currentTimeMillis();

        System.out.println("elapse: " + (end - start));
        list.forEach(campaign -> {
            System.out.println(campaign);
        });

    }

    @Test
    public void findAllByDonatorTest() {
        // long start = System.currentTimeMillis();
        // List<Long> idList = svCampaignRepository.findCampaignIdsByDonator(SVMemberGeneralEntity.create("DGM01"));
        
        // Iterable<SVCampaignEntity> list = svCampaignRepository.findAllByDonator(idList);
        
        // long end = System.currentTimeMillis();

        // System.out.println("elapse: " + (end - start));
        // list.forEach(campaign -> {
        //     System.out.println(campaign);
        // });

    }

    @Test
    public void findByIdTest() {
        // SVCampaignEntity entity = svCampaignRepository.findById(campaignId).get();
        // System.out.println(entity);
    }

}