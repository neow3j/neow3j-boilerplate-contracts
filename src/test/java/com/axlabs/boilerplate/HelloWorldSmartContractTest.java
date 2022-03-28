package com.axlabs.boilerplate;

import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.response.NeoInvokeFunction;
import io.neow3j.test.ContractTest;
import io.neow3j.test.ContractTestExtension;
import io.neow3j.test.DeployConfig;
import io.neow3j.test.DeployConfiguration;
import io.neow3j.types.ContractParameter;
import io.neow3j.types.Hash160;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static io.neow3j.types.ContractParameter.hash160;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContractTest(blockTime = 1, contracts = HelloWorldSmartContract.class)
public class HelloWorldSmartContractTest {

    private static final String GET_OWNER = "getOwner";
    private static final String GET_A_STRING = "getStaticValue";
    private static final String OWNER_ADDRESS = "NNSyinBZAr8HMhjj95MfkKD1PY7YWoDweR";

    @RegisterExtension
    private static ContractTestExtension ext = new ContractTestExtension();

    private static Neow3j neow3j;
    private static SmartContract contract;

    @BeforeAll
    public static void setUp() {
        neow3j = ext.getNeow3j();
        contract = ext.getDeployedContract(HelloWorldSmartContract.class);
    }

    @DeployConfig(HelloWorldSmartContract.class)
    public static DeployConfiguration configure() {
        DeployConfiguration config = new DeployConfiguration();
        ContractParameter owner = hash160(Hash160.fromAddress(OWNER_ADDRESS));
        config.setDeployParam(owner);
        config.setSubstitution("${placeholder}", "A string value.");
        return config;
    }

    @Test
    public void invokeGetOwner() throws IOException {
        NeoInvokeFunction result = contract.callInvokeFunction(GET_OWNER);
        assertEquals(result.getInvocationResult().getStack().get(0).getAddress(), OWNER_ADDRESS);
    }

    @Test
    public void invokeGetAString() throws IOException {
        NeoInvokeFunction result = contract.callInvokeFunction(GET_A_STRING);
        assertEquals(result.getInvocationResult().getStack().get(0).getString(), "A string value.");
    }

}
