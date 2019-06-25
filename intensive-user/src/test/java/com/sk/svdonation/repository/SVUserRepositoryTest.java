package com.sk.svdonation.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sk.svdonation.entity.SVMemberBaseEntity;
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
public class SVUserRepositoryTest {
    @Autowired
    SVMemberRepository svUserRepository;

    String password = "test";
    String walletAddress = "walletAddress";
    String privateKey = "privateKey";

    String donatorMemberId = "donator";
    SVMemberType donatorMemberType = SVMemberType.DONATOR;
    String memberName = "기부자";

    String foundationMemberId = "foundation";
    SVMemberType foundationMemberType = SVMemberType.FOUNDATION;
    String foundationName = "행복재단";

    @Before
    public void settup() {
        SVMemberGeneralEntity donator = SVMemberGeneralEntity.create(donatorMemberId, password, donatorMemberType, walletAddress, privateKey, memberName);
        svUserRepository.save(donator);
        SVMemberFoundationEntity foundation = SVMemberFoundationEntity.create(foundationMemberId, password, foundationMemberType, walletAddress, privateKey, foundationName);
        svUserRepository.save(foundation);
    }

    @After
    public void tearDown() {
        svUserRepository.deleteById(donatorMemberId);
        svUserRepository.deleteById(foundationMemberId);
    }

    @Test
    public void testFindById() {
        SVMemberBaseEntity findDonator = svUserRepository.findById(donatorMemberId).orElse(null);

        assertNotNull(findDonator);
        assertTrue(findDonator instanceof SVMemberGeneralEntity);
        assertEquals(donatorMemberId, findDonator.getMemberId());
        assertEquals(password, findDonator.getPassword());
        assertEquals(donatorMemberType, findDonator.getMemberType());
        assertEquals(walletAddress, findDonator.getWalletAddress());
        assertEquals(privateKey, findDonator.getPrivateKey());

        SVMemberBaseEntity findFoundation = svUserRepository.findById(foundationMemberId).orElse(null);

        assertNotNull(findFoundation);
        assertTrue(findFoundation instanceof SVMemberFoundationEntity);
        assertEquals(foundationMemberId, findFoundation.getMemberId());
        assertEquals(password, findFoundation.getPassword());
        assertEquals(foundationMemberType, findFoundation.getMemberType());
        assertEquals(walletAddress, findFoundation.getWalletAddress());
        assertEquals(privateKey, findFoundation.getPrivateKey());
    }
}