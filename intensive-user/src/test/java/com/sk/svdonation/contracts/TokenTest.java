package com.sk.svdonation.contracts;

import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.web3j.contracts.eip20.generated.ERC20;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class TokenTest {
    @Autowired
    @Qualifier("SVC")
    private ERC20 svc;

    @Autowired
    @Qualifier("SVP")
    private ERC20 svp;

    @Test
    public void getBalanceTest() {
        try {
            BigInteger svcBalance = svc.balanceOf("0x3ad7a871A1812d787375367dCfB09f438a21F093").send();
            BigInteger svpBalance = svp.balanceOf("0x3ad7a871A1812d787375367dCfB09f438a21F093").send();
            System.out.println("SVC Balance : " + svcBalance + ", SVP Balance : " + svpBalance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}