package com.sk.svdonation.service;

import java.util.concurrent.BlockingQueue;

import com.sk.svdonation.dto.SVEthTxDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SVEthReceiptCrawler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SVEthReceiptCrawler.class);

    private Web3j web3j;
    private BlockingQueue<SVEthTxDTO> receiptCrawlingQueue;

    @Override
    public void run() {
        while (true) {
            try {
                logger.info("WAIT SVEthReceiptCrawler[" + Thread.currentThread().getName() + "]");
                SVEthTxDTO txData = receiptCrawlingQueue.take();
                String txHash = txData.getTxHash();
                logger.info("RUN SVEthReceiptCrawler[" + Thread.currentThread().getName() + "] {txData: " + txData + "}");
                
                EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send();
                if(receipt.getResult() == null) {
                    logger.info("REPUT and WAIT SVEthReceiptCrawler[" + Thread.currentThread().getName() + "] {txData: " + txData + "}");
                    Thread.sleep(500);
                    receiptCrawlingQueue.put(txData);
                    continue;
                }

                txData.getSuccessCallBack().accept(receipt.getRawResponse());
                logger.info("SUCCESS and WAIT SVEthReceiptCrawler[" + Thread.currentThread().getName() + "] {receipt: " + receipt.getRawResponse() + "}");
            } catch(Exception e) {
                logger.error("SVEthReceiptCrawler[" + Thread.currentThread().getName() + "] Exception", e);
                break;
            }
        }
    }
}