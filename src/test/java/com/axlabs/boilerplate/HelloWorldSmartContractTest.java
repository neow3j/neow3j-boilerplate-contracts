package com.axlabs.boilerplate;

import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.core.response.NeoInvokeFunction;
import io.neow3j.test.ContractTest;
import io.neow3j.test.ContractTestExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <b>IMPORTANT:</b> The neow3j test framework (and with it its ContractTestExtension) do NOT support Neo 3.6.0 yet
 * (hence, the use of the 3.20.2 version for neow3j's devpack-test dependency in the build.gradle file). This means,
 * that any feature that was introduced with the Neo 3.6.0 release cannot be tested with this extension. Also, it
 * might be the case that a test case passes with Neo 3.5.0, but does not any longer with Neo 3.6.0. So, be aware of
 * this when using the test framework for now, and try to test critical parts with a separate Neo 3.6.0 node.
 */
@ContractTest(blockTime = 1, contracts = HelloWorldSmartContract.class)
public class HelloWorldSmartContractTest {

    private static final String GET_OWNER = "getOwner";
    private static final String GET_A_STRING = "getStaticValue";
    private static final String OWNER_ADDRESS = "NNSyinBZAr8HMhjj95MfkKD1PY7YWoDweR";

    private static SmartContract contract;

    @RegisterExtension
    public static final ContractTestExtension ext = new ContractTestExtension();

    @BeforeAll
    public static void setUp() {
        contract = ext.getDeployedContract(HelloWorldSmartContract.class);
    }

    @Test
    public void invokeGetOwner() throws IOException {
        NeoInvokeFunction result = contract.callInvokeFunction(GET_OWNER);
        assertEquals(result.getInvocationResult().getStack().get(0).getAddress(), OWNER_ADDRESS);
    }

    @Test
    public void invokeGetAString() throws IOException {
        NeoInvokeFunction result = contract.callInvokeFunction(GET_A_STRING);
        assertEquals(result.getInvocationResult().getStack().get(0).getString(), "Hello World!");
    }

}
