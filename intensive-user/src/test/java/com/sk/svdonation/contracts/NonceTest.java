package com.sk.svdonation.contracts;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.Test;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.http.HttpService;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class NonceTest {
    private static final String USER = "k0dueg9m5s";
    private static final String PASS = "IpydooBlFEk9CbFwAZuEFmgaCKYhAbXCKvlDuoR657U";
    private static final String RPC_ENDPOINT = "https://k0pbnr4wv3-k0q1mh3xdl-rpc.kr0-aws.kaleido.io"; // With https://

    @Test
    public void getNonceTest() {
        // Build an Authorization header using your app credentials
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        final String credentials = Credentials.basic(USER, PASS);
        clientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                return response.request().newBuilder().header("Authorization", credentials).build();
            }
        });

        // Create a Service object for web3 to connect to
        HttpService service = new HttpService(RPC_ENDPOINT, clientBuilder.build(), false);
        Web3j web3j = Web3j.build(service);

        // BigInteger privkey = new BigInteger(privateKey, 16);
        // ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
        
        EthGetTransactionCount ethGetTransactionCount = null;
        try {
            ethGetTransactionCount = web3j.ethGetTransactionCount("0x0e831F99df2d9759d906B5BeE718c386a4147636",
                    DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();
            System.out.println(nonce);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Test
    public void getAccountsTest() {
                // Build an Authorization header using your app credentials
                OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
                final String credentials = Credentials.basic(USER, PASS);
                clientBuilder.authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder().header("Authorization", credentials).build();
                    }
                });
        
                // Create a Service object for web3 to connect to
                HttpService service = new HttpService(RPC_ENDPOINT, clientBuilder.build(), false);
                Web3j web3j = Web3j.build(service);
        
                // BigInteger privkey = new BigInteger(privateKey, 16);
                // ECKeyPair ecKeyPair = ECKeyPair.create(privkey);
                
                try {
                    EthAccounts accounts = web3j.ethAccounts().send();
                    accounts.getAccounts().forEach(ac -> {
                        System.out.println(ac);
                    });
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        
    }
}