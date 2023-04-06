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
