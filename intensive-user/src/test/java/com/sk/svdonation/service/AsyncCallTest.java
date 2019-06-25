package com.sk.svdonation.service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import com.sk.svdonation.contracts.SVCoin;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import lombok.Data;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class AsyncCallTest {
    private static final Logger logger = LoggerFactory.getLogger(AsyncCallTest.class);
    private BlockingQueue<SVTxModel> txQueue = new LinkedBlockingQueue<>(10);
    private BlockingQueue<SVTxModel> receiptQueue = new LinkedBlockingQueue<>(10);

    private static final String USER = "k0dueg9m5s";
    private static final String PASS = "IpydooBlFEk9CbFwAZuEFmgaCKYhAbXCKvlDuoR657U";
    private static final String RPC_ENDPOINT = "https://k0pbnr4wv3-k0q1mh3xdl-rpc.kr0-aws.kaleido.io"; // With https://
    private static final String PRIVATE_KEY = "ffd4eb3b77d9950f73968ada857bdf19";
    private static final String SVCAddress = "0xed1a2a9026efc75D7e4452Feb2eDdd747585B89b";
    private static final String SVPAddress = "0xa6Fb1685979a95c81dE65363601F8740F7b8b846";

    private Web3j web3j;
    public String testMessage = "Test Member variable";

    @Before
    public void settup() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(USER, PASS);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        HttpService service = new HttpService(RPC_ENDPOINT, clientBuilder.build(), true);
        web3j = Web3j.build(service);
    }

    @Test
    public void asnycTest() throws IOException, InterruptedException {
        String publicKey = "0xAE43677343f09034568CB1812D2A55f63F075BAd";
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(publicKey, DefaultBlockParameterName.LATEST).send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        String data = SVCoin.BINARY;
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, BigInteger.valueOf(0), BigInteger.valueOf(4_500_000), BigInteger.valueOf(0), data);

        String privateKey = "c0a11e53ad739ebe6ae805f97c75d81bccfc87a7312b526f8f868a84377866fa";
        BigInteger bi_privateKey = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bi_privateKey);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, org.web3j.crypto.Credentials.create(ecKeyPair));
        String signedTx = Numeric.toHexString(signedMessage);

        SVTxModel txModel1 = new SVTxModel();
        txModel1.setSignedTx(signedTx);
        txModel1.setErrorCallBack(this::errorCallBack);
        txModel1.setSuccessCallBack(this::successCallBack);
        SVTxModel txModel2 = new SVTxModel();
        txModel2.setSignedTx(signedTx);
        txModel2.setErrorCallBack(this::errorCallBack);
        txModel2.setSuccessCallBack(this::successCallBack);
        SVTxModel txModel3 = new SVTxModel();
        txModel3.setSignedTx(signedTx); 
        txModel3.setErrorCallBack(this::errorCallBack);
        txModel3.setSuccessCallBack(this::successCallBack);
        SVTxModel txModel4 = new SVTxModel();
        txModel4.setSignedTx(signedTx);
        txModel4.setErrorCallBack(this::errorCallBack);
        txModel4.setSuccessCallBack(this::successCallBack);
        SVTxModel txModel5 = new SVTxModel();
        txModel5.setSignedTx(signedTx);
        txModel5.setErrorCallBack(this::errorCallBack);
        txModel5.setSuccessCallBack(this::successCallBack);

        new Thread(new SVTxSender(txQueue, receiptQueue, web3j)).start();
        new Thread(new SVTxSender(txQueue, receiptQueue, web3j)).start();
        new Thread(new SVTxSender(txQueue, receiptQueue, web3j)).start();
        new Thread(new SVTxSender(txQueue, receiptQueue, web3j)).start();
        new Thread(new SVTxSender(txQueue, receiptQueue, web3j)).start();
        new Thread(new SVTxReceiptCrawler(receiptQueue, web3j)).start();
        new Thread(new SVTxReceiptCrawler(receiptQueue, web3j)).start();
        new Thread(new SVTxReceiptCrawler(receiptQueue, web3j)).start();
        new Thread(new SVTxReceiptCrawler(receiptQueue, web3j)).start();
        new Thread(new SVTxReceiptCrawler(receiptQueue, web3j)).start();

        txQueue.put(txModel1);
        txQueue.put(txModel2);
        txQueue.put(txModel3);
        txQueue.put(txModel4);
        txQueue.put(txModel5);

        while(true) {

        }
    }

    public void errorCallBack(String message) {
        logger.info("CALL errorCallBack {message: " + message + "}");
        // System.out.println(message);
        // System.out.println(testMessage);
        // testMethod();
    }

    public void successCallBack(String message) {
        logger.info("CALL successCallBack {message: " + message + "}");
        // System.out.println(message);
        // System.out.println(testMessage);
        // testMethod();
    }

    public void testMethod() {
        System.out.println("Called by callback method");
    }
}

class SVTxSender implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SVTxSender.class);
    private Web3j web3j;
    BlockingQueue<SVTxModel> txQueue;
    BlockingQueue<SVTxModel> receiptQueue;

    SVTxSender(BlockingQueue<SVTxModel> txQueue, BlockingQueue<SVTxModel> receiptQueue, Web3j web3j) {
        this.txQueue = txQueue;
        this.receiptQueue = receiptQueue;
        this.web3j = web3j;
    }

    @Override
    public void run() {
        while (true) {
            try {
                logger.info("WAIT SVTxSender[" + Thread.currentThread().getName() + "]");
                SVTxModel txModel = txQueue.take();
                String signedTx = txModel.getSignedTx();
                logger.info("RUN SVTxSender[" + Thread.currentThread().getName() + "] {txModel: " + txModel + "}");
                EthSendTransaction result = web3j.ethSendRawTransaction(signedTx).send();

                if(result.getResult() == null) {
                    txModel.getErrorCallBack().accept(result.getError().getMessage());
                    continue;
                }
                txModel.setTxHash(result.getTransactionHash());
                receiptQueue.put(txModel);
                txModel.getSuccessCallBack().accept(result.getTransactionHash());
                logger.info("END and WAIT SVTxSender[" + Thread.currentThread().getName() + "]");
            } catch(Exception e) {
                logger.error("SVTxSender.run Exception", e);
                break;
            }
        }
    }

}

class SVTxReceiptCrawler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(SVTxReceiptCrawler.class);
    private Web3j web3j;
    BlockingQueue<SVTxModel> receiptQueue;

    SVTxReceiptCrawler(BlockingQueue<SVTxModel> receiptQueue, Web3j web3j) {
        this.receiptQueue = receiptQueue;
        this.web3j = web3j;
    }

    @Override
    public void run() {
        while (true) {
            try {
                logger.info("WAIT SVTxReceiptCrawler[" + Thread.currentThread().getName() + "]");
                SVTxModel txModel = receiptQueue.take();
                String txHash = txModel.getTxHash();
                logger.info("RUN SVTxReceiptCrawler[" + Thread.currentThread().getName() + "] {txModel: " + txModel + "}");
                
                EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(txHash).send();
                if(receipt.getResult() == null) {
                    Thread.sleep(500);
                    logger.info("REPUT SVTxReceiptCrawler[" + Thread.currentThread().getName() + "] {txModel: " + txModel + "}");
                    receiptQueue.put(txModel);
                    continue;
                }
                txModel.getSuccessCallBack().accept(receipt.getRawResponse());
                logger.info("END and WAIT SVTxReceiptCrawler[" + Thread.currentThread().getName() + "] {receipt: " + receipt.getRawResponse() + "}");
            } catch(Exception e) {
                logger.error("SVTxReceiptCrawler.run Exception", e);
                break;
            }
        }
    }
}

@Data
class SVTxModel {
    private String signedTx;
    private String txHash;
    private Consumer<String> errorCallBack;
    private Consumer<String> successCallBack;
}