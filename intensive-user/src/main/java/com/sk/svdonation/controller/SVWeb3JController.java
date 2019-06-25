package com.sk.svdonation.controller;

import java.util.HashMap;
import java.util.Map;

import com.sk.svdonation.dto.ResponseDTO;
import com.sk.svdonation.dto.SVTokenApproveDTO;
import com.sk.svdonation.dto.SVTokenMintDTO;
import com.sk.svdonation.dto.SVTokenTransferDTO;
import com.sk.svdonation.service.SVDonationService;
import com.sk.svdonation.service.SVWeb3JService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SVWeb3JController {
    @NonNull
    private SVWeb3JService web3jService;
    @NonNull
    private SVDonationService donationService;
    @Value("${svdonation.kaleido.address.svc}")
    private String svcAddress;
    @Value("${svdonation.kaleido.address.svp}")
    private String svpAddress;
    @Value("${svdonation.kaleido.chainid}")
    private String chainID;

    @GetMapping("/v1/web3j/nonce/{account}")
    public ResponseDTO getNonce(@PathVariable("account") String account) {
        log.info("CALL API SVWeb3JController.getNonce {account: " + account + "}");
        Map<String, Long> returnData = new HashMap<>();
        returnData.put("nonce", web3jService.getNonce(account));
        return new ResponseDTO.Builder().data(returnData).build();
    }

    @GetMapping("/v1/web3j/chainid")
    public ResponseDTO getChainId() {
        log.info("CALL API SVWeb3JController.getChainId");
        Map<String, String> returnData = new HashMap<>();
        returnData.put("chainid", chainID);
        return new ResponseDTO.Builder().data(returnData).build();
    }

    @GetMapping("/v1/web3j/token/address")
    public ResponseDTO getTokenAddress() {
        log.info("CALL API SVWeb3JController.getTokenAddress");
        Map<String, String> returnData = new HashMap<>();
        returnData.put("svc", svcAddress);
        returnData.put("svp", svpAddress);
        return new ResponseDTO.Builder().data(returnData).build();
    }

    @GetMapping("/v1/web3j/svcbalance/{account}")
    public ResponseDTO getSVCBalance(@PathVariable("account") String account) {
        log.info("CALL API SVWeb3JController.getSVCBalance {account: " + account + "}");
        Map<String, Long> returnData = new HashMap<>();
        returnData.put("balance", web3jService.getSVCBalance(account));
        return new ResponseDTO.Builder().data(returnData).build();
    }

    @GetMapping("/v1/web3j/lockedsvcbalance")
    public ResponseDTO getLockedSVCBalance(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); String
		userName = userDetails.getUsername();
        log.info("CALL API SVWeb3JController.getLockedSVCBalance {userName: " + userName + "}");
        
        Map<String, Long> returnData = new HashMap<>();
        long balance = donationService.getDonationReady(userName).stream().mapToLong(donation -> donation.getAmountOfSVC()).sum();
        returnData.put("balance", balance);
        return new ResponseDTO.Builder().data(returnData).build();
    }

    @GetMapping("/v1/web3j/svpbalance/{account}")
    public ResponseDTO getSVPBalance(@PathVariable("account") String account) {
        log.info("CALL API SVWeb3JController.getSVPBalance {account: " + account + "}");
        Map<String, Long> returnData = new HashMap<>();
        returnData.put("balance", web3jService.getSVPBalance(account));
        return new ResponseDTO.Builder().data(returnData).build();
    }
    
    @PutMapping("/v1/web3j/token/transfer")
    public ResponseDTO transferToken(@RequestBody SVTokenTransferDTO tokenTransfer) {
        log.info("CALL API SVWeb3JController.transferToken {tokenTransfer: " + tokenTransfer + "}");
        web3jService.transferToken(tokenTransfer);
        return new ResponseDTO.Builder().build();
    }

    @PutMapping("/v1/web3j/token/approve")
    public ResponseDTO approveToken(@RequestBody SVTokenApproveDTO tokenApprove) {
        log.info("CALL API SVWeb3JController.approveToken {tokenApprove: " + tokenApprove + "}");
        web3jService.approveToken(tokenApprove);
        return new ResponseDTO.Builder().build();
    }

    @PostMapping("/v1/web3j/token/mint")
    public ResponseDTO mintToken(@RequestBody SVTokenMintDTO tokenMint) {
        log.info("CALL API SVWeb3JController.mintToken {tokenMint: " + tokenMint + "}");
        web3jService.mintToken(tokenMint);
        return new ResponseDTO.Builder().build();
    }
}