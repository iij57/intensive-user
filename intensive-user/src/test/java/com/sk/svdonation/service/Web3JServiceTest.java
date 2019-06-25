package com.sk.svdonation.service;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import com.sk.svdonation.contracts.SVCoin;
import com.sk.svdonation.dto.SVTokenMintDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class Web3JServiceTest {
    @Autowired
    SVWeb3JService web3jService;

    @Autowired
    SVCoin svc;
    
    @Value("${svdonation.kaleido.address.svc}")
    private String svcAddress;
    @Value("${svdonation.kaleido.address.svp}")
    private String svpAddress;

    @Test
    public void getNonceTest() {
        long nonce = web3jService.getNonce("0x841D45E917683B7527D3A00DAe76a237a2054749");
        System.out.println("nonce: " + nonce);
    }

    @Test
    public void getBalanceTest() {
        long balance = web3jService.getSVCBalance("0x841D45E917683B7527D3A00DAe76a237a2054749");
        long balance2 = web3jService.getSVCBalance("0xCCfe183F75392671e60aF3e3285B8E9150E7d0f5");
        long balance3 = web3jService.getSVCBalance("0xB4F4DdbE20277c6658FD80a3E62B70c245D74B54");
        long balance4 = web3jService.getSVCBalance("0x067a40e0b07169e0706b3bae784051069a829744");
        System.out.println("balance: " + balance + ", balance2: " + balance2 + ", balace3: " + balance3 + ", balace4: " + balance4);
    }

    @Test
    public void transferTest() {
        // String addressOfDGM01 = "0x841D45E917683B7527D3A00DAe76a237a2054749";
        // web3jService.transferSVC(addressOfDGM01, 1_000);
        // long balance = web3jService.getSVCBalance(addressOfDGM01);
        // System.out.println("balance : " + balance);
        // assertTrue(balance > 0);
    }

    @Test
    public void mintTest() throws Exception {
        String address = "0x841D45E917683B7527D3A00DAe76a237a2054749";
        long svcBalance = web3jService.getSVCBalance(address);
        System.out.println("Balance: " + svcBalance);

        long mintAmount = 500000;
        web3jService.mintToken(new SVTokenMintDTO(address, mintAmount, ""));

        long finalBalance = web3jService.getSVCBalance(address);

        System.out.println("Balance: " + finalBalance);

        assertEquals(svcBalance + mintAmount, finalBalance);
    }
}