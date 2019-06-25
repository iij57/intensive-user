package com.sk.svdonation.service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.sk.svdonation.dto.SVCampaignPostDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.utils.Numeric;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SVCampaignServiceTest {
    @Autowired
    private SVCampaignService campaignService;
    @Autowired
    private SVWeb3JService svWeb3jService;
    @Autowired
    private Web3j web3j;

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

    @Test
    public void getCampaignsTest() {

        campaignService.getCampaigns().forEach(campaign -> {
            System.out.println(campaign);
        });
    }

    @Test
    public void getCampaignTest() {
        System.out.println(campaignService.getCampaign(1));
    }

    @Test
    public void convertTest() {
        LocalDateTime startA = LocalDateTime.parse("2019-05-09 14:13:15",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(startA);
    }

    @Test
    public void getReceiptTest() {
        try {
            EthGetTransactionReceipt dd = web3j
                    .ethGetTransactionReceipt("0xe47b3da38c4a70916ac4a483963d832307c0fed19e283894bd1fc42f3eadb4d3")
                    .send();

            System.out.println(dd.getTransactionReceipt());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void createCampaignTest() {
        // String publicKey = "0x0e831F99df2d9759d906B5BeE718c386a4147636";
        // long nonce = svWeb3jService.getNonce(publicKey);

        String data = "0x608060405234801561001057600080fd5b50604051606080610ff98339810180604052606081101561003057600080fd5b8101908080519060200190929190805190602001909291908051906020019092919050505033600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550826000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600481905550806003819055506000600660006101000a81548160ff0219169083600481111561010357fe5b0217905550600060088190555060006009819055507f0d968ed0843603b466b27a4b0092affa11344caafe6d80e695c16ef623fd42bd3360045483604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001935050505060405180910390a1505050610e5a8061019f6000396000f3fe608060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063590e1ae3146100675780635a6b26ba1461007e57806386d1a69f146100d9578063f14faf6f146100f0575b600080fd5b34801561007357600080fd5b5061007c61012b565b005b34801561008a57600080fd5b506100d7600480360360408110156100a157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506104d4565b005b3480156100e557600080fd5b506100ee6107b1565b005b3480156100fc57600080fd5b506101296004803603602081101561011357600080fd5b8101908080359060200190929190505050610956565b005b60011515600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160019054906101000a900460ff16151514151561018d57600080fd5b6000600481111561019a57fe5b600660009054906101000a900460ff1660048111156101b557fe5b14156101c4576101c36107b1565b5b600260048111156101d157fe5b600660009054906101000a900460ff1660048111156101ec57fe5b1415156101f857600080fd5b60003390506000600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000209050600015158160010160009054906101000a900460ff16151514151561026457600080fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd30846005546040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561035e57600080fd5b505af1158015610372573d6000803e3d6000fd5b505050506040513d602081101561038857600080fd5b8101908080519060200190929190505050506001600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160006101000a81548160ff0219169083151502179055507fbb28353e4598c3b9199101a66e0989549b659a59a54d2c27fbb183f1932c8e6d82600554604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1600160096000828254019250508190555060085460095414156104d0576004600660006101000a81548160ff0219169083600481111561049e57fe5b02179055507f8616bbbbad963e4e65b1366f1d75dfb63f9e9704bbbf91fb01bec70849906cf760405160405180910390a15b5050565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561053057600080fd5b600180600481111561053e57fe5b600660009054906101000a900460ff16600481111561055957fe5b14151561056557600080fd5b60028390806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3085856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156106c357600080fd5b505af11580156106d7573d6000803e3d6000fd5b505050506040513d60208110156106ed57600080fd5b810190808051906020019092919050505050816005600082825403925050819055506000600554141561073f576003600660006101000a81548160ff0219169083600481111561073957fe5b02179055505b7fb6e26e54bd578616699ab07efb4c2e322b45a80673803bbb259443d9fecee65283600554604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1505050565b60008060048111156107bf57fe5b600660009054906101000a900460ff1660048111156107da57fe5b1415156107e657600080fd5b6003544210151515610886576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f43757272656e742074696d65206973206265666f72652072656c65617365207481526020017f696d65000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6004546005541015156108f5576001600660006101000a81548160ff021916908360048111156108b257fe5b02179055507f36d4ea4d2a836eb91a99a36614ff84d373648ee61d232da708aa2eb96ef45c586005546040518082815260200191505060405180910390a1610953565b6002600660006101000a81548160ff0219169083600481111561091457fe5b02179055507f123de41493f6e442c2fdb676dd1c3d9f1e0a7b7531ca0cdb9f1026f7569e2bff6005546040518082815260200191505060405180910390a15b50565b806000811015151561096757600080fd5b806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663dd62ed3e33306040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060206040518083038186803b158015610a5657600080fd5b505afa158015610a6a573d6000803e3d6000fd5b505050506040513d6020811015610a8057600080fd5b810190808051906020019092919050505010151515610a9e57600080fd5b60006004811115610aab57fe5b600660009054906101000a900460ff166004811115610ac657fe5b141515610ad257600080fd5b60003390506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b158015610bcf57600080fd5b505af1158015610be3573d6000803e3d6000fd5b505050506040513d6020811015610bf957600080fd5b81019080805190602001909291905050505082600560008282540192505081905550600160086000828254019250508190555060011515600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160019054906101000a900460ff1615151415610cdd5782600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160008282540192505081905550610d8c565b60606040519081016040528084815260200160001515815260200160011515815250600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000820151816000015560208201518160010160006101000a81548160ff02191690831515021790555060408201518160010160016101000a81548160ff0219169083151502179055509050505b600454600554101515610dbe576001600660006101000a81548160ff02191690836004811115610db857fe5b02179055505b7f0553260a2e46b0577270d8992db02d30856ca880144c72d6e9503760946aef138184604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a150505056fea165627a7a7230582097cfeb488c30b9564cd61dd9bbc0def86a37127a03acf5f6b9e4e9b091018fe40029";

        // long now = LocalDateTime.now().plusDays(10).atZone(ZoneId.systemDefault()).toEpochSecond();
        // String encodedConstructor = FunctionEncoder.encodeConstructor(
        //                                                                 Arrays.asList(
        //                                                                     new Address("0x67a9c64f43b928ea5cc8f9aa93a74b68c7aefc5b"), 
        //                                                                     new Uint256(100_000_000l), 
        //                                                                     new Uint256(now)
        //                                                                 )
        //                                                             );
        // RawTransaction rawTransaction = RawTransaction.createContractTransaction(BigInteger.valueOf(nonce), BigInteger.valueOf(gasprice), BigInteger.valueOf(gaslimit), BigInteger.ZERO, data + encodedConstructor);

        // String privateKey = "1ea4cdb00867cbacd64c02341f0594570257648fa225e2cec88ef77af7604786";
        // BigInteger bi_privateKey = new BigInteger(privateKey, 16);
        // ECKeyPair ecKeyPair = ECKeyPair.create(bi_privateKey);

        // byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction,  org.web3j.crypto.Credentials.create(ecKeyPair));
        // String signedTx = Numeric.toHexString(signedMessage);
        // System.out.println("+++++++++++++++++++++++++++++++++++++++");
        // System.out.println(signedTx);
        SVCampaignPostDTO campaign = new SVCampaignPostDTO("TEST TITLE", "TEST CONTENT", "IMG URL", "2019-06-01 09:01:00", "2019-06-08 09:01:00", 10_000_000, data, "");
        
        campaignService.createCampaign(campaign, "Foundation01");
    }
}