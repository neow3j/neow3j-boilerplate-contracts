package io.neow3j;

import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3jExpress;
import io.neow3j.protocol.core.response.NeoInvokeFunction;
import io.neow3j.test.ContractTest;
import io.neow3j.test.ContractTestExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@ContractTest(blockTime = 1, contractClass = HelloWorldSmartContract.class)
public class HelloWorldSmartContractTest {

    private static final String BONGO_CAT = "bongoCat";

    @RegisterExtension
    private static ContractTestExtension ext = new ContractTestExtension();

    private Neow3jExpress neow3j;
    private SmartContract sc;

    public HelloWorldSmartContractTest(Neow3jExpress neow3j, SmartContract contract) {
        this.neow3j = neow3j;
        this.sc = contract;
    }

    @Test
    public void invokeBongoCat() throws IOException {
        NeoInvokeFunction result = sc.callInvokeFunction(BONGO_CAT);
        assertEquals(result.getInvocationResult().getStack().get(0).getString(), "neowwwwwwwwww");
    }

}
