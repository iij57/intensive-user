package com.sk.svdonation.service;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sk.svdonation.contracts.Campaign;
import com.sk.svdonation.contracts.SVCoin;
import com.sk.svdonation.contracts.SVPower;

import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Numeric;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class Web3JAPITest {
    private static final String USER = "k0dueg9m5s";
    private static final String PASS = "IpydooBlFEk9CbFwAZuEFmgaCKYhAbXCKvlDuoR657U";
    private static final String RPC_ENDPOINT = "https://k0pbnr4wv3-k0q1mh3xdl-rpc.kr0-aws.kaleido.io"; // With https://
    private static final String PRIVATE_KEY = "ffd4eb3b77d9950f73968ada857bdf19";
    private static final String SVCAddress = "0xed1a2a9026efc75D7e4452Feb2eDdd747585B89b";
    private static final String SVPAddress = "0xa6Fb1685979a95c81dE65363601F8740F7b8b846";

    private Web3j web3j;

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
    public void getBalance() {
    }

    @Test
    public void createAccount() {

        try {
            String password = "test";
            System.out.println("creating account start=================================================");
            for(int i = 0 ; i < 200 ; i++) {
                ECKeyPair keyPair = Keys.createEcKeyPair();
                WalletFile wallet = Wallet.createStandard(password, keyPair);
                System.out.println(keyPair.getPrivateKey().toString(16) + "," + wallet.getAddress());
            }
            System.out.println("creating account end=================================================");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    @Test
    public void accountsTest() throws IOException {
        EthAccounts accounts = web3j.ethAccounts().send();

        accounts.getAccounts().forEach(account -> {
            System.out.println(account);
        });
    }

    @Test
    public void deploySVC() {
        try {
            String publicKey = "0xAE43677343f09034568CB1812D2A55f63F075BAd";
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(publicKey, DefaultBlockParameterName.LATEST).send();
            BigInteger nonce = ethGetTransactionCount.getTransactionCount();

            long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
            String data = "";
            RawTransaction rawTransaction = RawTransaction.createContractTransaction(nonce, BigInteger.valueOf(0),
                    BigInteger.valueOf(4_500_000), BigInteger.valueOf(0), data);

            String privateKey = "c0a11e53ad739ebe6ae805f97c75d81bccfc87a7312b526f8f868a84377866fa";
            BigInteger bi_privateKey = new BigInteger(privateKey, 16);
            ECKeyPair ecKeyPair = ECKeyPair.create(bi_privateKey);

            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,
                    org.web3j.crypto.Credentials.create(ecKeyPair));
            String signedTx = Numeric.toHexString(signedMessage);
            System.out.println("+++++++++++++++++++++++++++++++++++++++");
            System.out.println(signedTx);
            
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
                System.out.println(receipt.getResult() == null ? "receipt.getResult() IS NULL"
                        : "receipt.getResult() IS NOT NULL");
                Thread.sleep(500);
            }

            // TODO Exception 정의 필요
            if (receipt.getResult() == null)
                throw new RuntimeException("Can't get transaction receipt!!");

        } catch (Exception e) {
            // TODO Exception 정의 필요
            throw new RuntimeException(e);
        }
    }

    @Test
    public void functionCallTest() throws Exception {
        EthGetTransactionCount ethGetTransactionCount = web3j
                .ethGetTransactionCount("0x841D45E917683B7527D3A00DAe76a237a2054749", DefaultBlockParameterName.LATEST)
                .send();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        Function function = new Function("mint", // function we're calling
                Arrays.asList(new Address("0xCCfe183F75392671e60aF3e3285B8E9150E7d0f5"), new Uint256(12345)), // Parameters
                                                                                                              // to pass
                                                                                                              // as
                                                                                                              // Solidity
                                                                                                              // Types
                Arrays.asList(new TypeReference<Bool>() {
                }));

        String encodedFunction = FunctionEncoder.encode(function);
        Transaction transaction = Transaction.createFunctionCallTransaction(
                "0x841D45E917683B7527D3A00DAe76a237a2054749", nonce, BigInteger.ZERO, BigInteger.valueOf(4_500_000l),
                "0xCCfe183F75392671e60aF3e3285B8E9150E7d0f5", encodedFunction);
        String privateKey = "948DF4F3A9C295482C94B746950320F3B79B91791D5D32C397EE6D40355B318B";
        BigInteger bi_privateKey = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bi_privateKey);

        // byte[] signedMessage = TransactionEncoder.signMessage(transaction,
        //         org.web3j.crypto.Credentials.create(ecKeyPair));

        org.web3j.protocol.core.methods.response.EthSendTransaction transactionResponse = web3j
                .ethSendTransaction(transaction).sendAsync().get();
        System.out.println(transactionResponse);
        String transactionHash = transactionResponse.getTransactionHash();

    }

    @Test
    public void mintTest() throws Exception {
        String privateKey = "c0a11e53ad739ebe6ae805f97c75d81bccfc87a7312b526f8f868a84377866fa";
        BigInteger bi_privateKey = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bi_privateKey);
        SVCoin svc = SVCoin.load("0x67a9c64f43b928ea5cc8f9aa93a74b68c7aefc5b", web3j, org.web3j.crypto.Credentials.create(ecKeyPair), new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(4500000)));

        String[] addresses = {
                            // "0x841D45E917683B7527D3A00DAe76a237a2054749",
                            // "0xCCfe183F75392671e60aF3e3285B8E9150E7d0f5",
                            // "0xFb636eDe68f14fd13B45CC6d34421A367FFBCB25",
                            // "0x3ad7a871A1812d787375367dCfB09f438a21F093",
                            // "0xfc1c4833F22f8EAB2eca7884476873368C7Eca73",
                            // "0xcB0B206e640a3A93F3004CB84021201Cb16FF063"
                            };
        for(String address : addresses) {
            long svcBalance = svc.balanceOf(new Address(address)).send().getValue().longValue();
            System.out.println("Balance: " + svcBalance);
    
            long mintAmount = 1000000;
            
            svc.mint(new Address(address), new Uint256(mintAmount)).send();
    
            long finalBalance = svc.balanceOf(new Address(address)).send().getValue().longValue();
    
            System.out.println("Balance: " + finalBalance);
    
            assertEquals(svcBalance + mintAmount, finalBalance);
        }
    }

    @Test
    public void mintSVCTest() throws Exception {
        String privateKey = "c0a11e53ad739ebe6ae805f97c75d81bccfc87a7312b526f8f868a84377866fa";
        BigInteger bi_privateKey = new BigInteger(privateKey, 16);
        ECKeyPair ecKeyPair = ECKeyPair.create(bi_privateKey);
        SVCoin svc = SVCoin.load("0x67a9c64f43b928ea5cc8f9aa93a74b68c7aefc5b", web3j, org.web3j.crypto.Credentials.create(ecKeyPair), new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(4500000)));
        // SVCoin svc = SVCoin.load("0x67a9c64f43b928ea5cc8f9aa93a74b68c7aefc5b", web3j, new ClientTransactionManager(web3j, "0xAE43677343f09034568CB1812D2A55f63F075BAd"), new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(4500000)));

        String[] addresses = {
                                "d5d5f840c212b0286bd95853db90be40ff5f17a3",
                                "d1dfe023919bdfc831e3324586dfe8e04c8012db",
                                "4cf8e84b4315e9805bf652e1b4d43d020438b8fa",
                                "bd3956548cde1935c69564bdf84feb2a1ad1d256",
                                "0ac51a6d512f4b2600114622d638a8170d7a6089",
                                "31cce34c5a4d47c484726e22738ac254e529643c",
                                "dcf58d94435c81af9b4e4e7e68d1175cdae94ea5",
                                "9a2735b4096e555ebe689331f5d28a9f77e819fe",
                                "e7100f3d0105df4c752c401db945daf9b33b4277",
                                "906862157ed3ea16071ab4a8bbcbc847aae65834",
                                "c9d0e02b78b5ac87a9bcc7c3e8b7bbdc02eb3f4c",
                                "4e647f348dabd9c0faac8202cd6071c60f1fada7",
                                "f2a55f542662e8f91931723ed3297c54e4f10b28",
                                "115df43cd44a4656a5d7ec01fca9a8acb27e9428",
                                "8a59d3b4f59c978c3537655aa4254261e0a29dda",
                                "27b615d3d5d20d5962569f635ccb3777fddfad5d",
                                "e108a5cff4abf7185356d47819cf8ad27850d241",
                                "39483ef30403fafc8f9be79ba9d79df39320f921",
                                "76263ce565d4982b853d5f249963e38a22aa902f",
                                "6714382be15ba6a70410a1cdf56259d527001e2c",
                                "9df8576a15f77f04447295e80aaf949c80eca64e",
                                "cbf2322c4365f29c3068ad80344db27462c9a82b",
                                "5f5636854766d0f40e2c6f08c7e1f066121ec5fb",
                                "10382d17c58d4e6a964e7586d798077eec4e8512",
                                "734367788a7958b861cb194065d3c41fb638683a",
                                "4b34a01fdec7fb279cc36b8ac5b25ef669e8cf88",
                                "7db6163a8f545a7ba970f9bf319f79dddfb2a07b",
                                "584b80bbb3f1d768a595506b6eb36d0a072875bb",
                                "a38785b615729901361b7e23e28c06fb657ff182",
                                "0f77e5d860b3b53827ce951ff2ab74cff9ba33a5",
                                "5008ea5b88a029820f2346115cb788a741eb5c60",
                                "088707e5180ae383e51ad6538b57402808d2c755",
                                "7d69f1969bb9cc8e53e4f31f06de7fed48a692ad",
                                "41f5f0f1c0e4b1c866bc8f8ae861fa713a1f5fc1",
                                "3b519f6e537053d6b07248e2f7ac34b8cee1c096",
                                "8b436967610fd22540a6aa6515de58687faf07b6",
                                "3beec4dc2b41a2944fc934f0c29e343cb8bf05d9",
                                "18a7c2fab6ff5333e38f8f209d2b38a1554d3533",
                                "a313a32a71303898303fcab74c7f7d8ec633ba5c",
                                "e0e89941bd1596e5d7a3216ce0938437267469d2",
                                "00ad9460033880c9cad94cb8c90568a12e0375a9",
                                "0eac5c049c99fe0d75625cba7ac8fda6d39443fb",
                                "7a8abdca2a42a0562993337cfe45dd10c6735c96",
                                "a79c40d148adfeb0bca5a2adaa0cae6c30950073",
                                "bbe6ffaf199103c98c2be39be28c72315ad78c27",
                                "1e43e9bbb774f8cba01b0d9091cc53f6251e8dee",
                                "6c08a56ca300eda54e8e547d9e5010fa36bf7257",
                                "73a1eb22894a69cefb0856c5af46cc9437ac1ccf",
                                "678dc403d70521ae5b06ce1ea7229194f0cf2919",
                                "5b160623c55b0961795ad09a822a7aaee0da1ce4",
                                "d18bfadf585e6598c3590821e546a4c0e991f5d0",
                                "5721092e36735b992807031e672d200e97b551f6",
                                "74cdcd56a67c43a2c0b1e1923aad8b4278e84cfe",
                                "f1beae55d73d2b2f1b4d063b0ee6aa4ddb2f1c45",
                                "5ce3889e6387b80bd452e76ff288736ac47685ee",
                                "353216ea61518f94e9dd11a3107ae61de045ab5d",
                                "543f0d48be71774b1cbfb75052d7f2da03337b68",
                                "97b56edcb6194c0b31781217ce7e2a6c89b91f7e",
                                "46e9fee491c44fe62506ec9a38fecdaaddba0a84",
                                "f348b46d015179dfd1cf65e2bd77a345ee400ae0",
                                "7372809d5f0fa8f9124c98b9c7f64010cd76c8f9",
                                "2deece4f67e114c9c4af1a370ee027c222a667ae",
                                "6da78ab457a4c3a08466a56ff525f892a54f754c",
                                "b9d5e75f7e8e984632f39e6df607577c752223ce",
                                "46813d3b63f3d5e3071a2b518f34ea14d2f8ad64",
                                "e83f14fd7628c418d16b665dd15b553a5824fb70",
                                "0f51943afa78b5e106efbf84da09d7e16000b259",
                                "56f17e707a7c817cf6164d7797c828670f9c8be1",
                                "42a66302efdc2a31b5e1b1a1c856bfc892ac0bb5",
                                "82f80a52b2f4a1531758ce5f9e27b9ff3fe4f566",
                                "db8062b51557d25ddd814738fa34f94da0697b25",
                                "9849fdf8da7ef9c7e91998b695fce17d23146321",
                                "f070455562d03c925dd48d3f2a7c71b2514f60dd",
                                "8e24db4a4190d502875e2eb4ae25c71d319bff7a",
                                "7d10bc3f7ad5caa2893e73ca3d181bcec9bd305c",
                                "31f0b9a360cbca39854c468a536c28ede431c9ae",
                                "63053b5d821a5fbf166efd2424a7f8a3a0b405c5",
                                "b85aad489cf72797a90202126853e38e0fc38b01",
                                "1ed02009742f89c893731342e1bba3ae71972cc7",
                                "d25cc25f80168fde97d2340a7379114141367120",
                                "404288adf73732f4783c2266a0fac2d264cb5f22",
                                "3d4b3e3af3c48a2cf20adddc191965303774914b",
                                "92d57fe0b564c6519834c09c3ed73cc813c14ef0",
                                "44a9d1b6ee63c555da03909a674023135133f6bb",
                                "69dc9e760304f71dd4d6488eb4d59e770c2e7675",
                                "d88f7d922483a23aec08564705e55974df7bcbcf",
                                "fb2bd443a13d0736b54fa3dc076bb00a6ca5900c",
                                "d59c4bb653e3770d87c185f6316fe7dc8fc112d7",
                                "c7459220f8bcbafaeebef25cc732833f6466db01",
                                "cb73f5505b482b1db5953c3254bde9527280e3c5",
                                "e8522875078d7ac6e0dcc039c16d9f662379b9bf",
                                "96c9f6a49377c16a2818b879ff3d9d0a8a68193f",
                                "bb126beed52f7129153bec612e381d8f75abec44",
                                "00244160ea1db03f3ef9ce13d26b64bcf4c66a40",
                                "6204129a00d68ea9e639668e603621222ba2d4bf",
                                "7016fb54bc25df2ee9806b98412ff0885d20ee58",
                                "36bf2741ef9802f55f3e98131e1b846a71669f5c",
                                "59ef73678fbaaad01a65af83944de563f06ed3d9",
                                "f197d9bc0f33e5d86ffda96be45171080f294480",
                                "c9775d6e3afbd0697fffb47bda387b5d8d21e04b",
                                "193f177ed85d95f270360eb8a75899db26c9754f",
                                "aed8b6250953f788c85376a40b64723acda184a1",
                                "ca85849a2db26cad04d592503b39b123aef309d2",
                                "1afb6d77e3f04aaccc7aa3e643b2f657d595cb6a",
                                "f9a80c3aea74c50258df322a432bb00c66962cda",
                                "22b7b62ee513724b2e8fe95db8f0479ceb87803c",
                                "9da7744680f0ce9c95f907397896ce8770428a94",
                                "855bc23500eb549beafa5b80d5a12c2c8ec08c44",
                                "7760b3bd5c3738c6dc1a8af6e6da06679befd651",
                                "38781a2d2be7e98b7be6802ec63e416f1926d578",
                                "4d0751fde17cf4402d3e5bc9939c34f54183a4b9",
                                "f41eff3a9954d398546e66d71a14de8b2610fc88",
                                "78d72d9104893e9bbb080d94ddf755c1a0fa56a9",
                                "31ea3e5b08e26ba59c77f86702e3afbfb41a77d0",
                                "8b6eda9d498fe4d5647ac080f9921fff48e2d3cc",
                                "9806a9bc3372d1c1fa51e529a15795be1fe1d94a",
                                "1cbb2d3cbfe98572fe176fd655c643653502dfcd",
                                "d0dfc1e61b2d08c1c687b5ee895e068bdabf2b25",
                                "490fa15aacc3c98632fc36378cf09ff1b54a7947",
                                "855b77957cf7235d40737f1a0987cf6038beb0a3",
                                "4a97bd9caf89b29cadf57d7a1add456073048919",
                                "ce8ee610c7c5503cbad810751230c2bd57d6cbbe",
                                "c5c6b062aa6175393705f2610dd5be6975170eea",
                                "669db9b854bfd25ae51dc581d296634641a25856",
                                "c770a5c1adc55a2b95f99cc6ca732747d2c6c059",
                                "a829a28174c91c4edfb9bce8487300a59dc1e9d0",
                                "9041e97cea7adebc4ef02a3e81fa672962a2b1e1",
                                "ba8ebfddaa30181d7b4cac954a5f0d5678b67978",
                                "42f93b556eb6d1ebf6e4b2838884b67f68f4993d",
                                "a95a4b767f7de9f068d2c60a3879f4aa8323b801",
                                "66a7ac86be816bcae6ba83a7ff761be645e1da87",
                                "9766e5fbd351e653dc7cdd8ec5f550dfdfb2b353",
                                "02dd784c78659904a147f095820b2ed57a840b0c",
                                "114672d6615fc7ec02dbcd9a4c7263346b2aab6f",
                                "9ce2cf574e5d9f61632917eb74de9322f87b8876",
                                "2020f88e50fb714dcb0c481043fdfeefa508be8a",
                                "31cce0b1726b5768be3ef81cb336ff003c42e5b3",
                                "55980fd1800016cd3e6d4ea7b5d2207a774d38c4",
                                "ba1f504f53bc10f8a12e35ba809a4b85dc1a756d",
                                "2916bd95e0f99dcc89b8187a64369d02f7fd25d5",
                                "97ee19f675bf6c7e5459245b31b00e69ad238dde",
                                "de1df4b462c6b53825dea87a04050a4c746f6f29",
                                "8df300286d57ffdd04e6fd07892341cd9ee7e329",
                                "fa6e0fe8d53cb77c27d106010c65547b40b8bb0f",
                                "b75a29038c115852997613420dfe6089210ebd04",
                                "cc3aea0a2178ba6aa39e0cb6518c4d99af2b11d4",
                                "c1cb4ff44040b84aa367bba900a58fb24b72bda9",
                                "7e8ee26ba0b633c060d5960411929f2bd3f5f1b9",
                                "4f1da3ebd99c50c9b60e220b2da482a83479e6cc",
                                "cf930d376d3919be5b29ed2e8e307f52cfc60f46",
                                "9daf71a8d172f8ee123d4b832375529622ba8cfe",
                                "a3a74b281a93d59ddd4e8f98ebe4d65fab175e3b",
                                "3ea006b81eaa0b69598adb83ad5a8fcd6d4ff128",
                                "1f9ea0b4e86502fdcc349c0f4fe611cef0d10b3d",
                                "a0125d3d2abbe9577ce9ac05ca06bb51a0ab9f5b",
                                "8c8aa5bfd6fe60b0ae886c5b515386bc1a0e6110",
                                "0b835514657fe42b2d3698281f27fbfe107843e9",
                                "98f9ddf64da5323f22fc6e1b8b8de71f07f26981",
                                "a570e9939323d76532222d89770737f6df43cfb1",
                                "fc1ba74048692e84dc3f282e430c387298a8af6c",
                                "ae6f26b12a1159499d95f5c05ee8725f9b1a6c2b",
                                "1745ae9038196359b2ef86ea1561b3d4d130a887",
                                "a595cbc22f9af056cf588c8f1b9a5a7dba056015",
                                "76d29da1a26ad89b6be40751ceeaa8659da367e9",
                                "38ea16593a9c7fe1775e766cb57b6df047e4ed24",
                                "6738e880a2148f0721626f11cb5ad49decf9ef6d",
                                "fa644ea62b3bf1e02d1b64a777a462718f2069ff",
                                "5ccfc45e894aa2d347ff0903e291f8c9cd08a400",
                                "4a3a4e5fa664df6037949a7c64238289f5b1c7f6",
                                "3fa82fcf6c0598b128c2ec54e35dd2884a2f3411",
                                "39e340502e4bd75ce79e3b4db1aa65bee88d220d",
                            };
        for(String address : addresses) {
            long svcBalance = svc.balanceOf(new Address(address)).send().getValue().longValue();
    
            long mintAmount = 500000 - svcBalance;
            if(mintAmount == 0) continue;
                        
            svc.mint(new Address(address), new Uint256(mintAmount)).send();
    
            long finalBalance = svc.balanceOf(new Address(address)).send().getValue().longValue();
    
            System.out.println("BalanceOf["+address+"]: " + finalBalance);
    
            assertEquals(svcBalance + mintAmount, finalBalance);
        }
    }

    @Test
    public void mintSVPTest() throws Exception {
        SVPower svp = SVPower.load("0xa6Fb1685979a95c81dE65363601F8740F7b8b846", web3j, new ClientTransactionManager(web3j, "0x8290DdB5c56e89413D5c044DC847CaDCeffDCE2e"), new StaticGasProvider(BigInteger.ZERO, BigInteger.valueOf(4500000)));

        String[] addresses = {
                                "d5d5f840c212b0286bd95853db90be40ff5f17a3",
                                // "d1dfe023919bdfc831e3324586dfe8e04c8012db",
                                // "4cf8e84b4315e9805bf652e1b4d43d020438b8fa",
                                // "bd3956548cde1935c69564bdf84feb2a1ad1d256",
                                // "0ac51a6d512f4b2600114622d638a8170d7a6089",
                                // "31cce34c5a4d47c484726e22738ac254e529643c",
                                // "dcf58d94435c81af9b4e4e7e68d1175cdae94ea5",
                                // "9a2735b4096e555ebe689331f5d28a9f77e819fe",
                                // "e7100f3d0105df4c752c401db945daf9b33b4277",
                                // "906862157ed3ea16071ab4a8bbcbc847aae65834",
                                // "c9d0e02b78b5ac87a9bcc7c3e8b7bbdc02eb3f4c",
                                // "4e647f348dabd9c0faac8202cd6071c60f1fada7",
                                // "f2a55f542662e8f91931723ed3297c54e4f10b28",
                                // "115df43cd44a4656a5d7ec01fca9a8acb27e9428",
                                // "8a59d3b4f59c978c3537655aa4254261e0a29dda",
                                // "27b615d3d5d20d5962569f635ccb3777fddfad5d",
                                // "e108a5cff4abf7185356d47819cf8ad27850d241",
                                // "39483ef30403fafc8f9be79ba9d79df39320f921",
                                // "76263ce565d4982b853d5f249963e38a22aa902f",
                                // "6714382be15ba6a70410a1cdf56259d527001e2c",
                                // "9df8576a15f77f04447295e80aaf949c80eca64e",
                                // "cbf2322c4365f29c3068ad80344db27462c9a82b",
                                // "5f5636854766d0f40e2c6f08c7e1f066121ec5fb",
                                // "10382d17c58d4e6a964e7586d798077eec4e8512",
                                // "734367788a7958b861cb194065d3c41fb638683a",
                                // "4b34a01fdec7fb279cc36b8ac5b25ef669e8cf88",
                                // "7db6163a8f545a7ba970f9bf319f79dddfb2a07b",
                                // "584b80bbb3f1d768a595506b6eb36d0a072875bb",
                                // "a38785b615729901361b7e23e28c06fb657ff182",
                                // "0f77e5d860b3b53827ce951ff2ab74cff9ba33a5",
                                // "5008ea5b88a029820f2346115cb788a741eb5c60",
                                // "088707e5180ae383e51ad6538b57402808d2c755",
                                // "7d69f1969bb9cc8e53e4f31f06de7fed48a692ad",
                                // "41f5f0f1c0e4b1c866bc8f8ae861fa713a1f5fc1",
                                // "3b519f6e537053d6b07248e2f7ac34b8cee1c096",
                                // "8b436967610fd22540a6aa6515de58687faf07b6",
                                // "3beec4dc2b41a2944fc934f0c29e343cb8bf05d9",
                                // "18a7c2fab6ff5333e38f8f209d2b38a1554d3533",
                                // "a313a32a71303898303fcab74c7f7d8ec633ba5c",
                                // "e0e89941bd1596e5d7a3216ce0938437267469d2",
                                // "00ad9460033880c9cad94cb8c90568a12e0375a9",
                                // "0eac5c049c99fe0d75625cba7ac8fda6d39443fb",
                                // "7a8abdca2a42a0562993337cfe45dd10c6735c96",
                                // "a79c40d148adfeb0bca5a2adaa0cae6c30950073",
                                // "bbe6ffaf199103c98c2be39be28c72315ad78c27",
                                // "1e43e9bbb774f8cba01b0d9091cc53f6251e8dee",
                                // "6c08a56ca300eda54e8e547d9e5010fa36bf7257",
                                // "73a1eb22894a69cefb0856c5af46cc9437ac1ccf",
                                // "678dc403d70521ae5b06ce1ea7229194f0cf2919",
                                // "5b160623c55b0961795ad09a822a7aaee0da1ce4",
                                // "d18bfadf585e6598c3590821e546a4c0e991f5d0",
                                // "5721092e36735b992807031e672d200e97b551f6",
                                // "74cdcd56a67c43a2c0b1e1923aad8b4278e84cfe",
                                // "f1beae55d73d2b2f1b4d063b0ee6aa4ddb2f1c45",
                                // "5ce3889e6387b80bd452e76ff288736ac47685ee",
                                // "353216ea61518f94e9dd11a3107ae61de045ab5d",
                                // "543f0d48be71774b1cbfb75052d7f2da03337b68",
                                // "97b56edcb6194c0b31781217ce7e2a6c89b91f7e",
                                // "46e9fee491c44fe62506ec9a38fecdaaddba0a84",
                                // "f348b46d015179dfd1cf65e2bd77a345ee400ae0",
                                // "7372809d5f0fa8f9124c98b9c7f64010cd76c8f9",
                                // "2deece4f67e114c9c4af1a370ee027c222a667ae",
                                // "6da78ab457a4c3a08466a56ff525f892a54f754c",
                                // "b9d5e75f7e8e984632f39e6df607577c752223ce",
                                // "46813d3b63f3d5e3071a2b518f34ea14d2f8ad64",
                                // "e83f14fd7628c418d16b665dd15b553a5824fb70",
                                // "0f51943afa78b5e106efbf84da09d7e16000b259",
                                // "56f17e707a7c817cf6164d7797c828670f9c8be1",
                                // "42a66302efdc2a31b5e1b1a1c856bfc892ac0bb5",
                                // "82f80a52b2f4a1531758ce5f9e27b9ff3fe4f566",
                                // "db8062b51557d25ddd814738fa34f94da0697b25",
                                // "9849fdf8da7ef9c7e91998b695fce17d23146321",
                                // "f070455562d03c925dd48d3f2a7c71b2514f60dd",
                                // "8e24db4a4190d502875e2eb4ae25c71d319bff7a",
                                // "7d10bc3f7ad5caa2893e73ca3d181bcec9bd305c",
                                // "31f0b9a360cbca39854c468a536c28ede431c9ae",
                                // "63053b5d821a5fbf166efd2424a7f8a3a0b405c5",
                                // "b85aad489cf72797a90202126853e38e0fc38b01",
                                // "1ed02009742f89c893731342e1bba3ae71972cc7",
                                // "d25cc25f80168fde97d2340a7379114141367120",
                                // "404288adf73732f4783c2266a0fac2d264cb5f22",
                                // "3d4b3e3af3c48a2cf20adddc191965303774914b",
                                // "92d57fe0b564c6519834c09c3ed73cc813c14ef0",
                                // "44a9d1b6ee63c555da03909a674023135133f6bb",
                                // "69dc9e760304f71dd4d6488eb4d59e770c2e7675",
                                // "d88f7d922483a23aec08564705e55974df7bcbcf",
                                // "fb2bd443a13d0736b54fa3dc076bb00a6ca5900c",
                                // "d59c4bb653e3770d87c185f6316fe7dc8fc112d7",
                                // "c7459220f8bcbafaeebef25cc732833f6466db01",
                                // "cb73f5505b482b1db5953c3254bde9527280e3c5",
                                // "e8522875078d7ac6e0dcc039c16d9f662379b9bf",
                                // "96c9f6a49377c16a2818b879ff3d9d0a8a68193f",
                                // "bb126beed52f7129153bec612e381d8f75abec44",
                                // "00244160ea1db03f3ef9ce13d26b64bcf4c66a40",
                                // "6204129a00d68ea9e639668e603621222ba2d4bf",
                                // "7016fb54bc25df2ee9806b98412ff0885d20ee58",
                                // "36bf2741ef9802f55f3e98131e1b846a71669f5c",
                                // "59ef73678fbaaad01a65af83944de563f06ed3d9",
                                // "f197d9bc0f33e5d86ffda96be45171080f294480",
                                // "c9775d6e3afbd0697fffb47bda387b5d8d21e04b",
                                // "193f177ed85d95f270360eb8a75899db26c9754f",
                                // "aed8b6250953f788c85376a40b64723acda184a1",
                                // "ca85849a2db26cad04d592503b39b123aef309d2",
                                // "1afb6d77e3f04aaccc7aa3e643b2f657d595cb6a",
                                // "f9a80c3aea74c50258df322a432bb00c66962cda",
                                // "22b7b62ee513724b2e8fe95db8f0479ceb87803c",
                                // "9da7744680f0ce9c95f907397896ce8770428a94",
                                // "855bc23500eb549beafa5b80d5a12c2c8ec08c44",
                                // "7760b3bd5c3738c6dc1a8af6e6da06679befd651",
                                // "38781a2d2be7e98b7be6802ec63e416f1926d578",
                                // "4d0751fde17cf4402d3e5bc9939c34f54183a4b9",
                                // "f41eff3a9954d398546e66d71a14de8b2610fc88",
                                // "78d72d9104893e9bbb080d94ddf755c1a0fa56a9",
                                // "31ea3e5b08e26ba59c77f86702e3afbfb41a77d0",
                                // "8b6eda9d498fe4d5647ac080f9921fff48e2d3cc",
                                // "9806a9bc3372d1c1fa51e529a15795be1fe1d94a",
                                // "1cbb2d3cbfe98572fe176fd655c643653502dfcd",
                                // "d0dfc1e61b2d08c1c687b5ee895e068bdabf2b25",
                                // "490fa15aacc3c98632fc36378cf09ff1b54a7947",
                                // "855b77957cf7235d40737f1a0987cf6038beb0a3",
                                // "4a97bd9caf89b29cadf57d7a1add456073048919",
                                // "ce8ee610c7c5503cbad810751230c2bd57d6cbbe",
                                // "c5c6b062aa6175393705f2610dd5be6975170eea",
                                // "669db9b854bfd25ae51dc581d296634641a25856",
                                // "c770a5c1adc55a2b95f99cc6ca732747d2c6c059",
                                // "a829a28174c91c4edfb9bce8487300a59dc1e9d0",
                                // "9041e97cea7adebc4ef02a3e81fa672962a2b1e1",
                                // "ba8ebfddaa30181d7b4cac954a5f0d5678b67978",
                                // "42f93b556eb6d1ebf6e4b2838884b67f68f4993d",
                                // "a95a4b767f7de9f068d2c60a3879f4aa8323b801",
                                // "66a7ac86be816bcae6ba83a7ff761be645e1da87",
                                // "9766e5fbd351e653dc7cdd8ec5f550dfdfb2b353",
                                // "02dd784c78659904a147f095820b2ed57a840b0c",
                                // "114672d6615fc7ec02dbcd9a4c7263346b2aab6f",
                                // "9ce2cf574e5d9f61632917eb74de9322f87b8876",
                                // "2020f88e50fb714dcb0c481043fdfeefa508be8a",
                                // "31cce0b1726b5768be3ef81cb336ff003c42e5b3",
                                // "55980fd1800016cd3e6d4ea7b5d2207a774d38c4",
                                // "ba1f504f53bc10f8a12e35ba809a4b85dc1a756d",
                                // "2916bd95e0f99dcc89b8187a64369d02f7fd25d5",
                                // "97ee19f675bf6c7e5459245b31b00e69ad238dde",
                                // "de1df4b462c6b53825dea87a04050a4c746f6f29",
                                // "8df300286d57ffdd04e6fd07892341cd9ee7e329",
                                // "fa6e0fe8d53cb77c27d106010c65547b40b8bb0f",
                                // "b75a29038c115852997613420dfe6089210ebd04",
                                // "cc3aea0a2178ba6aa39e0cb6518c4d99af2b11d4",
                                // "c1cb4ff44040b84aa367bba900a58fb24b72bda9",
                                // "7e8ee26ba0b633c060d5960411929f2bd3f5f1b9",
                                // "4f1da3ebd99c50c9b60e220b2da482a83479e6cc",
                                // "cf930d376d3919be5b29ed2e8e307f52cfc60f46",
                                // "9daf71a8d172f8ee123d4b832375529622ba8cfe",
                                // "a3a74b281a93d59ddd4e8f98ebe4d65fab175e3b",
                                // "3ea006b81eaa0b69598adb83ad5a8fcd6d4ff128",
                                // "1f9ea0b4e86502fdcc349c0f4fe611cef0d10b3d",
                                // "a0125d3d2abbe9577ce9ac05ca06bb51a0ab9f5b",
                                // "8c8aa5bfd6fe60b0ae886c5b515386bc1a0e6110",
                                // "0b835514657fe42b2d3698281f27fbfe107843e9",
                                // "98f9ddf64da5323f22fc6e1b8b8de71f07f26981",
                                // "a570e9939323d76532222d89770737f6df43cfb1",
                                // "fc1ba74048692e84dc3f282e430c387298a8af6c",
                                // "ae6f26b12a1159499d95f5c05ee8725f9b1a6c2b",
                                // "1745ae9038196359b2ef86ea1561b3d4d130a887",
                                // "a595cbc22f9af056cf588c8f1b9a5a7dba056015",
                                // "76d29da1a26ad89b6be40751ceeaa8659da367e9",
                                // "38ea16593a9c7fe1775e766cb57b6df047e4ed24",
                                // "6738e880a2148f0721626f11cb5ad49decf9ef6d",
                                // "fa644ea62b3bf1e02d1b64a777a462718f2069ff",
                                // "5ccfc45e894aa2d347ff0903e291f8c9cd08a400",
                                // "4a3a4e5fa664df6037949a7c64238289f5b1c7f6",
                                // "3fa82fcf6c0598b128c2ec54e35dd2884a2f3411",
                                // "39e340502e4bd75ce79e3b4db1aa65bee88d220d",
                            };
        for(String address : addresses) {
            long svpBalance = svp.balanceOf(new Address(address)).send().getValue().longValue();
    
            long mintAmount = 50 - svpBalance;
            if(mintAmount == 0) continue;
            
            svp.mint(new Address(address), new Uint256(mintAmount)).send();
            long finalBalance = svp.balanceOf(new Address(address)).send().getValue().longValue();
    
            System.out.println("BalanceOf["+address+"]: " + finalBalance);
    
            assertEquals(svpBalance + mintAmount, finalBalance);
        }
    }


    @Test
    public void bigIntTest() {
        System.out.println(new BigInteger("67a9c64f43b928ea5cc8f9aa93a74b68c7aefc5", 16));
    }

    @Test
    public void receiptTest() throws IOException {
        EthGetTransactionReceipt receipt = web3j.ethGetTransactionReceipt("0xcd9566786cc202f660be110efa5e8ec59e2fc2b82fa747298d877348e296446b").send();
        EthGetTransactionReceipt receipt2 = web3j.ethGetTransactionReceipt("0x86ed2c79f964eab53e162cfcfe1d41f0176a9596afa969a07488ec0708de8237").send();

        System.out.println(receipt.getRawResponse());
        System.out.println(receipt2 .getRawResponse());
    }

    @Test
    public void deployCampaign() throws Exception {
        long now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();

        // deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Address _svc, Address _svp, Uint256 _goalAmount, Uint256 _releaseTime) {
        Campaign campign = Campaign.deploy(
                                            web3j,
                                            new ClientTransactionManager(
                                                                            web3j,
                                                                            "0x8290DdB5c56e89413D5c044DC847CaDCeffDCE2e"),
                                            new StaticGasProvider(
                                                                    BigInteger.ZERO,
                                                                    BigInteger.valueOf(4500000)),
                                            new Address("0x67a9c64f43b928ea5cc8f9aa93a74b68c7aefc5b"),
                                            new Address("0xa6Fb1685979a95c81dE65363601F8740F7b8b846"),
                                            new Uint256(1000000000),
                                            new Uint256(now)).send();

        System.out.println();
    }
}