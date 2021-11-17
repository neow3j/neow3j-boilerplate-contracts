package org.example;

import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3jExpress;
import io.neow3j.protocol.core.response.NeoInvokeFunction;
import io.neow3j.test.ContractTest;
import io.neow3j.test.ContractTestExtension;
import io.neow3j.test.DeployConfig;
import io.neow3j.test.DeployConfiguration;
import io.neow3j.types.ContractParameter;
import io.neow3j.types.Hash160;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;

import static io.neow3j.types.ContractParameter.hash160;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContractTest(blockTime = 1, contracts = HelloWorldSmartContract.class)
public class HelloWorldSmartContractTest {

    private static final String GET_OWNER = "getOwner";
    private static final String OWNER_ADDRESS = "NXXazKH39yNFWWZF5MJ8tEN98VYHwzn7g3";

    @RegisterExtension
    private static ContractTestExtension ext = new ContractTestExtension();

    private Neow3jExpress neow3j;
    private SmartContract contract;

    public HelloWorldSmartContractTest() {
        neow3j = ext.getNeow3j();
        contract = ext.getDeployedContract(HelloWorldSmartContract.class);
    }

    @Test
    public void invokeGetOwner() throws IOException {
        NeoInvokeFunction result = contract.callInvokeFunction(GET_OWNER);
        assertEquals(result.getInvocationResult().getStack().get(0).getAddress(), OWNER_ADDRESS);
    }

    @DeployConfig(HelloWorldSmartContract.class)
    public static void configure(DeployConfiguration conf) {
        ContractParameter owner = hash160(Hash160.fromAddress(OWNER_ADDRESS));
        conf.setDeployParam(owner);
    }

}
