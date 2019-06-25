package com.sk.svdonation.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SVDonationServiceTest {
    @Autowired
    SVDonationService donationService;

    @Test
    public void getDonationsByCampaignTest() {
        donationService.getDonations(1l).forEach(dto -> {
            System.out.println(dto);
        });
    }

    @Test
    public void getDonationsByMemberTest() {
        donationService.getDonations("DGM01").forEach(dto -> {
            System.out.println(dto);
        });
    }
}