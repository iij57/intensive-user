package com.sk.svdonation.dto;

import io.reactivex.functions.Consumer;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class SVEthTxDTO {
    private String signedTx;
    private String txHash;
    private Consumer<String> successCallBack;
    private Consumer<String> errorCallBack;
}