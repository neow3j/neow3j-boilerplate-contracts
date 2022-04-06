package com.axlabs.boilerplate;

import io.neow3j.contract.GasToken;
import io.neow3j.contract.NeoToken;
import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.response.NeoApplicationLog;
import io.neow3j.protocol.core.response.NeoInvokeFunction;
import io.neow3j.protocol.core.response.NeoSendRawTransaction;
import io.neow3j.test.ContractTest;
import io.neow3j.test.ContractTestExtension;
import io.neow3j.test.DeployConfig;
import io.neow3j.test.DeployConfiguration;
import io.neow3j.transaction.AccountSigner;
import io.neow3j.transaction.ContractSigner;
import io.neow3j.transaction.Transaction;
import io.neow3j.transaction.TransactionBuilder;
import io.neow3j.transaction.exceptions.TransactionConfigurationException;
import io.neow3j.types.ContractParameter;
import io.neow3j.types.Hash256;
import io.neow3j.utils.Await;
import io.neow3j.wallet.Account;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static io.neow3j.types.ContractParameter.hash160;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ContractTest(
        contracts = HelloWorldSmartContract.class,
        blockTime = 1,
        configFile = "default.neo-express",
        batchFile = "setup.batch"
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HelloWorldSmartContractTest {

    @RegisterExtension
    private static ContractTestExtension ext = new ContractTestExtension();

    private static final String ALICE_ADDRESS = "NM7Aky765FG8NhhwtxjXRx7jEL1cnw7PBP";
    private static final String BOB_ADDRESS = "NZpsgXn9VQQoLexpuXJsrX8BsoyAhKUyiX";

    private static Neow3j neow3j;
    private static SmartContract contract;
    private static Account alice;
    private static Account bob;

    @DeployConfig(HelloWorldSmartContract.class)
    public static DeployConfiguration configure() throws Exception {
        DeployConfiguration config = new DeployConfiguration();
        config.setDeployParam(ContractParameter.hash160(ext.getAccount(ALICE_ADDRESS)));
        return config;
    }

    @BeforeAll
    public static void setUp() throws Exception {
        neow3j = ext.getNeow3j();
        contract = ext.getDeployedContract(HelloWorldSmartContract.class);
        alice = ext.getAccount(ALICE_ADDRESS);
        bob = ext.getAccount(BOB_ADDRESS);
    }

    // 1./2. Test the deployment and getting the contract owner
    @Test
    public void testGetOwner() throws IOException {
        NeoInvokeFunction result = contract.callInvokeFunction("getContractOwner");
        assertEquals(result.getInvocationResult().getStack().get(0).getAddress(), alice.getAddress());
    }

    // 3. Change the contract owner
    @Test
    public void testChangeOwner() throws Throwable {
        NeoInvokeFunction result = contract.callInvokeFunction("getContractOwner");
        assertThat(result.getInvocationResult().getStack(), hasSize(1));
        assertThat(result.getInvocationResult().getStack().get(0).getAddress(), is(alice.getAddress()));

        TransactionBuilder b = contract.invokeFunction("changeContractOwner", hash160(bob));
        Transaction tx = b.signers(AccountSigner.calledByEntry(alice)).sign();
        NeoSendRawTransaction response = tx.send();
        assertFalse(response.hasError());

        Await.waitUntilTransactionIsExecuted(response.getSendRawTransaction().getHash(), ext.getNeow3j());

        String newOwner = contract.callInvokeFunction("getContractOwner")
                .getInvocationResult().getStack().get(0).getAddress();
        assertThat(newOwner, is(bob.getAddress()));

        b = contract.invokeFunction("changeContractOwner", hash160(alice));
        tx = b.signers(AccountSigner.calledByEntry(bob)).sign();
        response = tx.send();
        assertFalse(response.hasError());

        Await.waitUntilTransactionIsExecuted(response.getSendRawTransaction().getHash(), ext.getNeow3j());

        newOwner = contract.callInvokeFunction("getContractOwner")
                .getInvocationResult().getStack().get(0).getAddress();
        assertThat(newOwner, is(alice.getAddress()));
    }

    // 4.0 Receive GAS
    @Order(1)
    @Test
    public void testReceiveGas() throws Throwable {
        GasToken gasToken = new GasToken(neow3j);
        BigInteger contractBalanceBefore = gasToken.getBalanceOf(contract.getScriptHash());

        BigInteger amount = gasToken.toFractions(BigDecimal.valueOf(12.345));
        Hash256 txHash = gasToken.transfer(bob, contract.getScriptHash(), amount)
                .signers(AccountSigner.calledByEntry(bob))
                .sign()
                .send()
                .getSendRawTransaction()
                .getHash();

        Await.waitUntilTransactionIsExecuted(txHash, neow3j);

        BigInteger contractBalanceAfter = gasToken.getBalanceOf(contract.getScriptHash());
        assertThat(contractBalanceAfter, is(contractBalanceBefore.add(amount)));

        // 5. Check that event was fired
        NeoApplicationLog log = neow3j.getApplicationLog(txHash).send().getApplicationLog();
        NeoApplicationLog.Execution exec = log.getExecutions().get(0);
        assertThat(exec.getNotifications(), hasSize(2));
        NeoApplicationLog.Execution.Notification notification1 = exec.getNotifications().get(1);
        assertThat(notification1.getEventName(), is("onGasPayment"));
        assertThat(notification1.getContract(), is(contract.getScriptHash()));
        assertThat(notification1.getState().getList().get(0).getAddress(), is(bob.getAddress()));
        assertThat(notification1.getState().getList().get(1).getInteger(), is(amount));
    }

    // 4.1 Refuse NEO
    @Test
    public void testRefuseNeo() {
        NeoToken neoToken = new NeoToken(neow3j);
        BigInteger amount = BigInteger.valueOf(22);

        TransactionConfigurationException thrown =
                Assert.assertThrows(TransactionConfigurationException.class,
                        () -> neoToken.transfer(bob, contract.getScriptHash(), amount)
                                .signers(AccountSigner.calledByEntry(bob))
                                .sign()
                );
        assertThat(thrown.getMessage(), containsString("Only GAS accepted."));
    }

    // 6. Withdraw GAS from contract
    @Order(4)
    @Test
    public void testWithdrawGas() throws Throwable {
        GasToken gasToken = new GasToken(neow3j);
        BigInteger contractBalanceBefore = gasToken.getBalanceOf(contract.getScriptHash());
        assertThat(contractBalanceBefore, is(gasToken.toFractions(BigDecimal.valueOf(12.345))));

        Hash256 txHash = gasToken.transfer(contract.getScriptHash(), alice.getScriptHash(), contractBalanceBefore)
                .signers(
                        AccountSigner.calledByEntry(alice),
                        ContractSigner.calledByEntry(contract.getScriptHash())
                )
                .sign()
                .send()
                .getSendRawTransaction()
                .getHash();

        Await.waitUntilTransactionIsExecuted(txHash, neow3j);

        BigInteger contractBalanceAfter = gasToken.getBalanceOf(contract.getScriptHash());
        assertThat(contractBalanceAfter, is(BigInteger.ZERO));
    }

    // 7.0 Call another contract
    @Order(2)
    @Test
    public void testCallAnotherContract() throws IOException {
        BigInteger contractBalance = contract.callInvokeFunction("getContractGasBalance")
                .getInvocationResult().getStack().get(0).getInteger();
        assertThat(contractBalance, is(new BigInteger("1234500000")));
    }

    // 7.1 Call another contract using a wrapper class
    @Order(3)
    @Test
    public void testCallAnotherContractWithWrapper() throws IOException {
        BigInteger contractBalance = contract.callInvokeFunction("getContractGasBalanceWithWrapper")
                .getInvocationResult().getStack().get(0).getInteger();
        assertThat(contractBalance, is(BigInteger.valueOf(1234500000)));
    }

}
