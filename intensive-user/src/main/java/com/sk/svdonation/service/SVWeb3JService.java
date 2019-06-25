package com.sk.svdonation.service;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import com.sk.svdonation.contracts.SVCoin;
import com.sk.svdonation.contracts.SVPower;
import com.sk.svdonation.dto.SVEthTxDTO;
import com.sk.svdonation.dto.SVTokenApproveDTO;
import com.sk.svdonation.dto.SVTokenMintDTO;
import com.sk.svdonation.dto.SVTokenTransferDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

@Service
public class SVWeb3JService {
    private static final Logger logger = LoggerFactory.getLogger(SVWeb3JService.class);

    private Web3j web3j;
    private SVCoin svc;
    private SVPower svp;

    private BlockingQueue<SVEthTxDTO> txSendQueue;
    private BlockingQueue<SVEthTxDTO> receiptCrawlingQueue;

    @Value("${svdonation.kaleido.gasprice}")
    private long gasPrice;
    @Value("${svdonation.kaleido.gaslimit}")
    private long gasLimit;
    @Value("${svdonation.ethqueue.txqueuesize}")
    private int txQueueSize;
    @Value("${svdonation.ethqueue.receiptqueuesize}")
    private int receiptQueueSize;
    @Value("${svdonation.ethqueue.txsendercount}")
    private int txSenderCount;
    @Value("${svdonation.ethqueue.receiptcrawlercount}")
    private int receiptCrawlerCount;

    public SVWeb3JService(Web3j web3j, SVCoin svc, SVPower svp) {
        this.web3j = web3j;
        this.svc = svc;
        this.svp = svp;
        
    }

    @PostConstruct
    public void postContstruct() {
        logger.info("CREATE Ethereum transaction queue {txQueueSize: " + txQueueSize + ", receiptQueueSize: " + receiptQueueSize + "}");
        txSendQueue = new LinkedBlockingQueue<>(txQueueSize);
        receiptCrawlingQueue = new LinkedBlockingQueue<>(receiptQueueSize);

        ExecutorService txSenderThreadPool = Executors.newFixedThreadPool(txSenderCount);
        IntStream.range(0, txSenderCount).forEach(n -> {
            txSenderThreadPool.execute(new SVEthTxSender(web3j, txSendQueue, receiptCrawlingQueue));
        });

        ExecutorService receiptCrawlerThreadPool = Executors.newFixedThreadPool(receiptCrawlerCount);
        IntStream.range(0, receiptCrawlerCount).forEach(n -> {
            receiptCrawlerThreadPool.execute(new SVEthReceiptCrawler(web3j, receiptCrawlingQueue));
        });
    }

    public long getNonce(String address) {
        try {
            logger.info("CALL SVWeb3JService.getNonce {address: " + address + "}");
            EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            logger.info("RETURN SVWeb3JService.getNonce {nonce: " + nonce + "}");
            return nonce.longValue();
        } catch (Exception e) {
            logger.error("SVWeb3JService.getNonce Exception", e);
        }
        return -1l;
    }

    public long getSVCBalance(String address) {
        try {
            logger.info("CALL SVWeb3JService.getSVCBalance {address: " + address + "}");
            BigInteger svcBalance = svc.balanceOf(new Address(address)).send().getValue();
            logger.info("RETURN SVWeb3JService.getSVCBalance {svcBalance: " + svcBalance + "}");
            return svcBalance.longValue();
        } catch (Exception e) {
            logger.error("SVWeb3JService.getSVCBalance Exception", e);
            throw new RuntimeException("SVWeb3JService.getSVCBalance Exception", e);
        }
    }

    public long getSVPBalance(String address) {
        try {
            logger.info("CALL SVWeb3JService.getSVPBalance {address: " + address + "}");
            BigInteger svpBalance = svp.balanceOf(new Address(address)).send().getValue();
            logger.info("RETURN SVWeb3JService.getSVPBalance {svpBalance: " + svpBalance + "}");
            return svpBalance.longValue();
        } catch (Exception e) {
            logger.error("SVWeb3JService.getSVPBalance Exception", e);
            throw new RuntimeException("SVWeb3JService.getSVPBalance Exception", e);
        }
    }

    public void transferToken(SVTokenTransferDTO tokenTransfer) {
        logger.info("CALL SVWeb3JService.transferToken {tokenTransfer: " + tokenTransfer + "}");
        sendRawTransactionAndReturnReceipt(tokenTransfer.getSignedTx());
        logger.info("RETURN SVWeb3JService.transferToken {}");
    }

    public void approveToken(SVTokenApproveDTO tokenApprove) {
        logger.info("CALL SVWeb3JService.approveToken {tokenApprove: " + tokenApprove + "}");
        sendRawTransactionAndReturnReceipt(tokenApprove.getSignedTx());
        logger.info("RETURN SVWeb3JService.approveToken {}");
    }

    public void mintToken(SVTokenMintDTO tokenMint) {
        logger.info("CALL SVWeb3JService.mintToken {tokenMint: " + tokenMint + "}");
        try {
            svc.mint(new Address(tokenMint.getTo()), new Uint256(tokenMint.getValue())).send();
        } catch (Exception e) {
            logger.error("SVWeb3JService.mintToken Exception", e);
            // TODO Exception 정의 필요
            throw new RuntimeException("SVWeb3JService.mintToken Exception", e);
        }
        logger.info("RETURN SVWeb3JService.mintToken {}");
    }

    public String createContractSignedTransaction(long nonce, String contractData, String encodedConstructor,
            String privateKey) {
        logger.info("CALL SVWeb3JService.createContractSignedTransaction {nonce: " + nonce + ", contractData: "
                + contractData + ", encodedConstructor: " + encodedConstructor + ", privateKey: " + privateKey + "}");
        RawTransaction rawTransaction = RawTransaction.createContractTransaction(BigInteger.valueOf(nonce),
                BigInteger.valueOf(gasPrice), BigInteger.valueOf(gasLimit), BigInteger.ZERO,
                contractData + encodedConstructor);

        BigInteger bi_privateKey = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bi_privateKey);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, Credentials.create(ecKeyPair));
        String signedTx = Numeric.toHexString(signedMessage);
        logger.info("RETURN SVWeb3JService.createContractSignedTransaction {signedTx: " + signedTx + "}");
        return signedTx;
    }

    public EthGetTransactionReceipt sendRawTransactionAndReturnReceipt(String signedTx) {
        try {
            logger.info("CALL SVWeb3JService.sendRawTransactionAndReturnReceipt {signedTx: " + signedTx + "}");
            EthSendTransaction result = web3j.ethSendRawTransaction(signedTx).send();
            // result.getResult 가 NULL인경우 블록체인 영역에서 오류가 발생.
            // RuntimeException을 던져서 transaction을 rollback한다.
            // TODO Exception 정의 필요
            if (result.getResult() == null)
                throw new RuntimeException(result.getError().getMessage());

            // TODO 블록체인 트랜잭션 완료 시점에 따라서 아래 로직은 큐로 처리하는게 좋을 것 같아요...
            // 트랜잭션 전송 후 바로 receipt를 가져오면 null이 나오는 경우가 있으므로 5회정도 시도 한다.
            int repeatCnt = 5;
            EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt(result.getTransactionHash()).send();
            while (receipt.getResult() == null && repeatCnt > 0) {
                repeatCnt--;
                receipt = web3j.ethGetTransactionReceipt(result.getTransactionHash()).send();
                logger.debug(receipt.getResult() == null ? "receipt.getResult() IS NULL"
                        : "receipt.getResult() IS NOT NULL");
                Thread.sleep(500);
            }

            // TODO Exception 정의 필요
            if (receipt.getResult() == null) throw new RuntimeException("Can't get transaction receipt!!");
            logger.info("RETURN SVWeb3JService.sendRawTransactionAndReturnReceipt {receipt: " + receipt.getRawResponse() + "}");
            return receipt;
        } catch (Exception e) {
            logger.error("SVWeb3JService.sendRawTransactionAndReturnReceipt Exception", e);
            // TODO Exception 정의 필요
            throw new RuntimeException("SVWeb3JService.sendRawTransactionAndReturnReceipt Exception", e);
        }
    }

    public void sendRawTransactionAsync(SVEthTxDTO txData) {
        logger.info("CALL SVWeb3JService.sendRawTransactionAsync {txData: " + txData + "}");
        try {
            txSendQueue.add(txData);
        } catch (Exception e) {
            logger.error("SVWeb3JService.sendRawTransactionAsync Exception", e);
            // TODO Exception 정의 필요
            throw new RuntimeException("SVWeb3JService.sendRawTransactionAsync Exception", e);
        }
    }

    public String sendFunctionCallTransaction(String from, String contractAddress, String encodedFunction) {
        try {
            logger.info("CALL SVWeb3JService.sendFunctionCallTransaction {from: " + from + ", contractAddress: " + contractAddress + ", encodedFunction: " + encodedFunction + "}");
            long nonce = getNonce(from);
            Transaction transaction = Transaction.createFunctionCallTransaction(from, BigInteger.valueOf(nonce), BigInteger.valueOf(gasPrice), BigInteger.valueOf(gasLimit), contractAddress, encodedFunction);
            EthSendTransaction response = web3j.ethSendTransaction(transaction).send();
            logger.info("RETURN SVWeb3JService.sendFunctionCallTransaction {response: " + response.getRawResponse() + "}");
            return response.getTransactionHash();
        } catch(Exception e) {
            logger.error("SVWeb3JService.sendFunctionCallTransaction Exception", e);
            throw new RuntimeException("SVWeb3JService.sendFunctionCallTransaction Exception", e);
        }
    }
}