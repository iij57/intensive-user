package com.sk.svdonation;

import java.io.IOException;
import java.math.BigInteger;

import com.sk.svdonation.contracts.SVCoin;
import com.sk.svdonation.contracts.SVPower;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

@Configuration
public class Web3JConfig {
    @Value("${svdonation.kaleido.user}")
    private String user;
    @Value("${svdonation.kaleido.password}")
    private String password;
    @Value("${svdonation.kaleido.rpc-endpoint}")
    private String rpcEndpoint;
    @Value("${svdonation.kaleido.privatekey}")
    private String privateKey;
    @Value("${svdonation.kaleido.gasprice}")
    private long gasprice;
    @Value("${svdonation.kaleido.gaslimit}")
    private long gaslimit;
    @Value("${svdonation.kaleido.tokenowner}")
    private String tokenOwner;
    @Value("${svdonation.kaleido.address.svc}")
    private String svcAddress;
    @Value("${svdonation.kaleido.address.svp}")
    private String svpAddress;

    @Bean
    public Web3j web3j() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(user, password);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        HttpService service = new HttpService(rpcEndpoint, clientBuilder.build(), true);
        Web3j web3j = Web3j.build(service);
        // EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, "0xdc0a274214cc23A5bb58C2751663fA364432DCC3");
        // Event event = new Event("NewDonation", Arrays.<TypeReference<?>>asList(new TypeReference<Uint>(true) {}, new TypeReference<Address>(false) {}, new TypeReference<Address>(false) {}, new TypeReference<Uint>(false) {}, new TypeReference<Uint>(false) {}));
        // filter.addSingleTopic(EventEncoder.encode(event));
        // web3j.blockFlowable(true).subscribe(block -> {
        //     System.out.println("web3j.blockFlowable==========================");
        //     System.out.println(block);
        //     System.out.println("web3j.blockFlowable==========================");
        // });
        // web3j.transactionFlowable().subscribe(tx -> {
        //     System.out.println("web3j.transactionFlowable==========================");
        //     System.out.println(tx);
        //     System.out.println("web3j.transactionFlowable==========================");
        // });
        // web3j.pendingTransactionFlowable().subscribe(tx -> {
        //     System.out.println("web3j.pendingTransactionFlowable==========================");
        //     System.out.println(tx);
        //     System.out.println("web3j.pendingTransactionFlowable==========================");
        // });
        // web3j.ethLogFlowable(filter).subscribe(log -> {
        //     System.out.println("web3j.ethLogFlowable==========================");
        //     System.out.println(log);
        //     System.out.println("web3j.ethLogFlowable==========================");
        // });
        return web3j;
    }

    @Bean(name = "SVC")
    public SVCoin svc(Web3j web3j) {
        BigInteger privkey = new BigInteger(tokenOwner, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
        
        SVCoin svc = SVCoin.load(svcAddress, web3j, org.web3j.crypto.Credentials.create(ecKeyPair), new StaticGasProvider(BigInteger.valueOf(gasprice), BigInteger.valueOf(gaslimit)));
        return svc;
    }

    @Bean(name = "SVP")
    public SVPower svp(Web3j web3j) {
        BigInteger privkey = new BigInteger(tokenOwner, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
        
        SVPower svp = SVPower.load(svpAddress, web3j, org.web3j.crypto.Credentials.create(ecKeyPair), new StaticGasProvider(BigInteger.valueOf(gasprice), BigInteger.valueOf(gaslimit)));
        return svp;
    }
}