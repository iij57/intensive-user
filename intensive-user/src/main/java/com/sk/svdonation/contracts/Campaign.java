package com.sk.svdonation.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class Campaign extends Contract {
    private static final String BINARY = "0x608060405234801561001057600080fd5b506040516080806119b98339810180604052608081101561003057600080fd5b810190808051906020019092919080519060200190929190805190602001909291908051906020019092919050505033600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550836000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555082600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600581905550806004819055506000600760006101000a81548160ff0219169083600481111561014e57fe5b021790555060006009819055506000600a819055507f0d968ed0843603b466b27a4b0092affa11344caafe6d80e695c16ef623fd42bd3360055483604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828152602001935050505060405180910390a1505050506117ce806101eb6000396000f3fe60806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630121b93f14610072578063590e1ae3146100ad5780635a6b26ba146100c457806386d1a69f1461011f578063f14faf6f14610136575b600080fd5b34801561007e57600080fd5b506100ab6004803603602081101561009557600080fd5b8101908080359060200190929190505050610171565b005b3480156100b957600080fd5b506100c261055c565b005b3480156100d057600080fd5b5061011d600480360360408110156100e757600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610ab2565b005b34801561012b57600080fd5b50610134610f41565b005b34801561014257600080fd5b5061016f6004803603602081101561015957600080fd5b8101908080359060200190929190505050611175565b005b80600081101515156101eb576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f506c6561736520646f6e617465206d6f7265207468616e203020746f6b656e0081525060200191505060405180910390fd5b80600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663dd62ed3e33306040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060206040518083038186803b1580156102db57600080fd5b505afa1580156102ef573d6000803e3d6000fd5b505050506040513d602081101561030557600080fd5b8101908080519060200190929190505050101515156103b2576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e8152602001807f596f75206861766520746f20617070726f766520796f75722053565020666f7281526020017f20746869732043616d706169676e00000000000000000000000000000000000081525060400191505060405180910390fd5b6000339050600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b1580156104b057600080fd5b505af11580156104c4573d6000803e3d6000fd5b505050506040513d60208110156104da57600080fd5b8101908080519060200190929190505050507ff668ead05c744b9178e571d2edb452e72baf6529c8d72160e64e59b50d865bd08184604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1505050565b60011515600860003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160019054906101000a900460ff16151514151561064d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260228152602001807f596f7520617265206e6f7420646f6e6f72206f6620746869732063616d70616981526020017f676e00000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6000600481111561065a57fe5b600760009054906101000a900460ff16600481111561067557fe5b141561068457610683610f41565b5b6002600481111561069157fe5b600760009054906101000a900460ff1660048111156106ac57fe5b141515610747576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260298152602001807f524546554e44203a20546869732063616d706169676e2073746174757320697381526020017f206e6f74204661696c000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60003390506000600860008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000209050600015158160010160009054906101000a900460ff161515141515610842576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260268152602001807f524546554e44203a20596f75207265717565737420616c72656164792072656681526020017f756e64696e67000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd30846006546040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561093c57600080fd5b505af1158015610950573d6000803e3d6000fd5b505050506040513d602081101561096657600080fd5b8101908080519060200190929190505050506001600860008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160006101000a81548160ff0219169083151502179055507fbb28353e4598c3b9199101a66e0989549b659a59a54d2c27fbb183f1932c8e6d82600654604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a16001600a60008282540192505081905550600954600a541415610aae576004600760006101000a81548160ff02191690836004811115610a7c57fe5b02179055507f8616bbbbad963e4e65b1366f1d75dfb63f9e9704bbbf91fb01bec70849906cf760405160405180910390a15b5050565b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610b9d576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f596f7520617265206e6f74206167656e6379206f6620746869732063616d706181526020017f69676e000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b6001806004811115610bab57fe5b600760009054906101000a900460ff166004811115610bc657fe5b141515610c61576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001807f546869732043616d706169676e20737461747573206973206e6f74207269676881526020017f742073746174757300000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60008260065403111515610d29576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260478152602001807f48657265206973206e6f20656e6f7567682053564320666f722062656e65666981526020017f63696172792e20506c656173652061646a75737420616d6f756e74206f66207781526020017f697468647261770000000000000000000000000000000000000000000000000081525060600191505060405180910390fd5b60038390806001815401808255809150509060018203906000526020600020016000909192909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663a9059cbb84846040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200182815260200192505050602060405180830381600087803b158015610e5357600080fd5b505af1158015610e67573d6000803e3d6000fd5b505050506040513d6020811015610e7d57600080fd5b8101908080519060200190929190505050508160066000828254039250508190555060006006541415610ecf576003600760006101000a81548160ff02191690836004811115610ec957fe5b02179055505b7fb6e26e54bd578616699ab07efb4c2e322b45a80673803bbb259443d9fecee65283600654604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a1505050565b6000806004811115610f4f57fe5b600760009054906101000a900460ff166004811115610f6a57fe5b141515611005576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260288152602001807f546869732043616d706169676e20737461747573206973206e6f74207269676881526020017f742073746174757300000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b60045442101515156110a5576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260238152602001807f43757272656e742074696d65206973206265666f72652072656c65617365207481526020017f696d65000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390fd5b600554600654101515611114576001600760006101000a81548160ff021916908360048111156110d157fe5b02179055507f36d4ea4d2a836eb91a99a36614ff84d373648ee61d232da708aa2eb96ef45c586006546040518082815260200191505060405180910390a1611172565b6002600760006101000a81548160ff0219169083600481111561113357fe5b02179055507f123de41493f6e442c2fdb676dd1c3d9f1e0a7b7531ca0cdb9f1026f7569e2bff6006546040518082815260200191505060405180910390a15b50565b80600081101515156111ef576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252601f8152602001807f506c6561736520646f6e617465206d6f7265207468616e203020746f6b656e0081525060200191505060405180910390fd5b806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663dd62ed3e33306040518363ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019250505060206040518083038186803b1580156112de57600080fd5b505afa1580156112f2573d6000803e3d6000fd5b505050506040513d602081101561130857600080fd5b8101908080519060200190929190505050101515156113b5576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602e8152602001807f596f75206861766520746f20617070726f766520796f75722053564320666f7281526020017f20746869732043616d706169676e00000000000000000000000000000000000081525060400191505060405180910390fd5b600060048111156113c257fe5b600760009054906101000a900460ff1660048111156113dd57fe5b141515611478576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252602f8152602001807f444f4e415445203a20546869732063616d706169676e2073746174757320697381526020017f206e6f742050726f63656564696e67000000000000000000000000000000000081525060400191505060405180910390fd5b60003390506000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166323b872dd3330866040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019350505050602060405180830381600087803b15801561157557600080fd5b505af1158015611589573d6000803e3d6000fd5b505050506040513d602081101561159f57600080fd5b81019080805190602001909291905050505082600660008282540192505081905550600160096000828254019250508190555060011515600860008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060010160019054906101000a900460ff16151514156116835782600860008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060000160008282540192505081905550611732565b60606040519081016040528084815260200160001515815260200160011515815250600860008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000820151816000015560208201518160010160006101000a81548160ff02191690831515021790555060408201518160010160016101000a81548160ff0219169083151502179055509050505b7f0553260a2e46b0577270d8992db02d30856ca880144c72d6e9503760946aef138184604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018281526020019250505060405180910390a150505056fea165627a7a7230582000db0298507044f1aebb4f12a77fa9f4b2efe282d950ee2b9f51a700ebd7f2710029";

    public static final String FUNC_DONATE = "donate";

    public static final String FUNC_VOTE = "vote";

    public static final String FUNC_RELEASE = "release";

    public static final String FUNC_WITHDRAWAL = "withdrawal";

    public static final String FUNC_REFUND = "refund";

    public static final Event NEWDONATION_EVENT = new Event("NewDonation", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event DONATE_EVENT = new Event("Donate", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event VOTE_EVENT = new Event("Vote", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event SUCCESS_EVENT = new Event("Success", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event FAIL_EVENT = new Event("Fail", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event WITHDRAWAL_EVENT = new Event("WithDrawal", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REFUND_EVENT = new Event("Refund", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event REFUNDED_EVENT = new Event("Refunded", 
            Arrays.<TypeReference<?>>asList());
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected Campaign(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Campaign(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Campaign(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Campaign(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<NewDonationEventResponse> getNewDonationEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(NEWDONATION_EVENT, transactionReceipt);
        ArrayList<NewDonationEventResponse> responses = new ArrayList<NewDonationEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewDonationEventResponse typedResponse = new NewDonationEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.beneficiary = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.goalAmount = (Uint256) eventValues.getNonIndexedValues().get(1);
            typedResponse.releaseTime = (Uint256) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<NewDonationEventResponse> newDonationEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, NewDonationEventResponse>() {
            @Override
            public NewDonationEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(NEWDONATION_EVENT, log);
                NewDonationEventResponse typedResponse = new NewDonationEventResponse();
                typedResponse.log = log;
                typedResponse.beneficiary = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse.goalAmount = (Uint256) eventValues.getNonIndexedValues().get(1);
                typedResponse.releaseTime = (Uint256) eventValues.getNonIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public Flowable<NewDonationEventResponse> newDonationEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(NEWDONATION_EVENT));
        return newDonationEventFlowable(filter);
    }

    public List<DonateEventResponse> getDonateEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DONATE_EVENT, transactionReceipt);
        ArrayList<DonateEventResponse> responses = new ArrayList<DonateEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DonateEventResponse typedResponse = new DonateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.donor = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DonateEventResponse> donateEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DonateEventResponse>() {
            @Override
            public DonateEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DONATE_EVENT, log);
                DonateEventResponse typedResponse = new DonateEventResponse();
                typedResponse.log = log;
                typedResponse.donor = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Flowable<DonateEventResponse> donateEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DONATE_EVENT));
        return donateEventFlowable(filter);
    }

    public List<VoteEventResponse> getVoteEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(VOTE_EVENT, transactionReceipt);
        ArrayList<VoteEventResponse> responses = new ArrayList<VoteEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            VoteEventResponse typedResponse = new VoteEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.voter = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<VoteEventResponse> voteEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, VoteEventResponse>() {
            @Override
            public VoteEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(VOTE_EVENT, log);
                VoteEventResponse typedResponse = new VoteEventResponse();
                typedResponse.log = log;
                typedResponse.voter = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Flowable<VoteEventResponse> voteEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(VOTE_EVENT));
        return voteEventFlowable(filter);
    }

    public List<SuccessEventResponse> getSuccessEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(SUCCESS_EVENT, transactionReceipt);
        ArrayList<SuccessEventResponse> responses = new ArrayList<SuccessEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            SuccessEventResponse typedResponse = new SuccessEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<SuccessEventResponse> successEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, SuccessEventResponse>() {
            @Override
            public SuccessEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(SUCCESS_EVENT, log);
                SuccessEventResponse typedResponse = new SuccessEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Flowable<SuccessEventResponse> successEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(SUCCESS_EVENT));
        return successEventFlowable(filter);
    }

    public List<FailEventResponse> getFailEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(FAIL_EVENT, transactionReceipt);
        ArrayList<FailEventResponse> responses = new ArrayList<FailEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FailEventResponse typedResponse = new FailEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<FailEventResponse> failEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, FailEventResponse>() {
            @Override
            public FailEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(FAIL_EVENT, log);
                FailEventResponse typedResponse = new FailEventResponse();
                typedResponse.log = log;
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(0);
                return typedResponse;
            }
        });
    }

    public Flowable<FailEventResponse> failEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FAIL_EVENT));
        return failEventFlowable(filter);
    }

    public List<WithDrawalEventResponse> getWithDrawalEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(WITHDRAWAL_EVENT, transactionReceipt);
        ArrayList<WithDrawalEventResponse> responses = new ArrayList<WithDrawalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            WithDrawalEventResponse typedResponse = new WithDrawalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.beneficiary = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<WithDrawalEventResponse> withDrawalEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, WithDrawalEventResponse>() {
            @Override
            public WithDrawalEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(WITHDRAWAL_EVENT, log);
                WithDrawalEventResponse typedResponse = new WithDrawalEventResponse();
                typedResponse.log = log;
                typedResponse.beneficiary = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Flowable<WithDrawalEventResponse> withDrawalEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(WITHDRAWAL_EVENT));
        return withDrawalEventFlowable(filter);
    }

    public List<RefundEventResponse> getRefundEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REFUND_EVENT, transactionReceipt);
        ArrayList<RefundEventResponse> responses = new ArrayList<RefundEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RefundEventResponse typedResponse = new RefundEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.donor = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RefundEventResponse> refundEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RefundEventResponse>() {
            @Override
            public RefundEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REFUND_EVENT, log);
                RefundEventResponse typedResponse = new RefundEventResponse();
                typedResponse.log = log;
                typedResponse.donor = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse.amount = (Uint256) eventValues.getNonIndexedValues().get(1);
                return typedResponse;
            }
        });
    }

    public Flowable<RefundEventResponse> refundEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REFUND_EVENT));
        return refundEventFlowable(filter);
    }

    public List<RefundedEventResponse> getRefundedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REFUNDED_EVENT, transactionReceipt);
        ArrayList<RefundedEventResponse> responses = new ArrayList<RefundedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RefundedEventResponse typedResponse = new RefundedEventResponse();
            typedResponse.log = eventValues.getLog();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RefundedEventResponse> refundedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RefundedEventResponse>() {
            @Override
            public RefundedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REFUNDED_EVENT, log);
                RefundedEventResponse typedResponse = new RefundedEventResponse();
                typedResponse.log = log;
                return typedResponse;
            }
        });
    }

    public Flowable<RefundedEventResponse> refundedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REFUNDED_EVENT));
        return refundedEventFlowable(filter);
    }

    public RemoteCall<TransactionReceipt> donate(Uint256 _amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DONATE, 
                Arrays.<Type>asList(_amount), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> vote(Uint256 _amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_VOTE, 
                Arrays.<Type>asList(_amount), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> release() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RELEASE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> withdrawal(Address _beneficiary, Uint256 _amount) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWAL, 
                Arrays.<Type>asList(_beneficiary, _amount), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> refund() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REFUND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Campaign load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Campaign(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Campaign load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Campaign(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Campaign load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Campaign(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Campaign load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Campaign(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Campaign> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Address _svc, Address _svp, Uint256 _goalAmount, Uint256 _releaseTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_svc, _svp, _goalAmount, _releaseTime));
        return deployRemoteCall(Campaign.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Campaign> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Address _svc, Address _svp, Uint256 _goalAmount, Uint256 _releaseTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_svc, _svp, _goalAmount, _releaseTime));
        return deployRemoteCall(Campaign.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Campaign> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Address _svc, Address _svp, Uint256 _goalAmount, Uint256 _releaseTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_svc, _svp, _goalAmount, _releaseTime));
        return deployRemoteCall(Campaign.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Campaign> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Address _svc, Address _svp, Uint256 _goalAmount, Uint256 _releaseTime) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_svc, _svp, _goalAmount, _releaseTime));
        return deployRemoteCall(Campaign.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class NewDonationEventResponse {
        public Log log;

        public Address beneficiary;

        public Uint256 goalAmount;

        public Uint256 releaseTime;
    }

    public static class DonateEventResponse {
        public Log log;

        public Address donor;

        public Uint256 amount;
    }

    public static class VoteEventResponse {
        public Log log;

        public Address voter;

        public Uint256 amount;
    }

    public static class SuccessEventResponse {
        public Log log;

        public Uint256 amount;
    }

    public static class FailEventResponse {
        public Log log;

        public Uint256 amount;
    }

    public static class WithDrawalEventResponse {
        public Log log;

        public Address beneficiary;

        public Uint256 amount;
    }

    public static class RefundEventResponse {
        public Log log;

        public Address donor;

        public Uint256 amount;
    }

    public static class RefundedEventResponse {
        public Log log;
    }
}
