package com.sk.svdonation.service;

import java.util.concurrent.BlockingQueue;

import com.sk.svdonation.dto.SVEthTxDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SVEthTxSender implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SVEthTxSender.class);

    private Web3j web3j;
    private BlockingQueue<SVEthTxDTO> txSendQueue;
    private BlockingQueue<SVEthTxDTO> receiptCrawlingQueue;

    @Override
    public void run() {
        while (true) {
            try {
                logger.info("WAIT SVEthTxSender[" + Thread.currentThread().getName() + "]");
                SVEthTxDTO txData = txSendQueue.take();
                String signedTx = txData.getSignedTx();
                logger.info("RUN SVEthTxSender[" + Thread.currentThread().getName() + "] {txData: " + txData + "}");
                EthSendTransaction result = web3j.ethSendRawTransaction(signedTx).send();

                if(result.getResult() == null) {
                    logger.error("ERROR and WAIT SVEthTxSender[" + Thread.currentThread().getName() + "] {error: " + result.getError().getMessage() + "}");
                    if(txData.getErrorCallBack() != null) txData.getErrorCallBack().accept(result.getError().getMessage());
                    continue;
                }
                SVEthTxDTO newQueueData = SVEthTxDTO.builder().signedTx(txData.getSignedTx()).txHash(result.getTransactionHash()).successCallBack(txData.getSuccessCallBack()).errorCallBack(txData.getErrorCallBack()).build();
                receiptCrawlingQueue.put(newQueueData);
                logger.info("SUCCESS and WAIT SVTxSender[" + Thread.currentThread().getName() + "]");
            } catch(Exception e) {
                logger.error("SVTxSender[" + Thread.currentThread().getName() + "] Exception", e);
                break;
            }
        }
    }
}