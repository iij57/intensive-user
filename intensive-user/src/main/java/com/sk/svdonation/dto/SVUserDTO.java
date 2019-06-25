package com.sk.svdonation.dto;

import lombok.Value;

@Value
public class SVUserDTO {
    private String id;
    private String type;
    private String walletAddress;
    private String privateKey;
}