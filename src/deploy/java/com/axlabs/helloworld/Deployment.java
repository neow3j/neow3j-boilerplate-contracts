package com.axlabs.helloworld;

import io.neow3j.compiler.CompilationUnit;
import io.neow3j.compiler.Compiler;
import io.neow3j.contract.ContractManagement;
import io.neow3j.contract.GasToken;
import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.response.NeoApplicationLog;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.AccountSigner;
import io.neow3j.transaction.TransactionBuilder;
import io.neow3j.types.Hash160;
import io.neow3j.types.Hash256;
import io.neow3j.types.NeoVMStateType;
import io.neow3j.utils.Await;
import io.neow3j.wallet.Account;

import java.util.HashMap;
import java.util.Map;

import static io.neow3j.types.ContractParameter.hash160;

public class Deployment {

    private static final String ALICE_WIF = "KzrHihgvHGpF9urkSbrbRcgrxSuVhpDWkSfWvSg97pJ5YgbdHKCQ";
    private static final String NODE = "http://localhost:50012";

    public static void main(String[] args) throws Throwable {
        Neow3j neow3j = Neow3j.build(new HttpService(NODE));

        Account deploymentAccount = Account.fromWIF(ALICE_WIF);
        if (new GasToken(neow3j).getBalanceOf(deploymentAccount).intValue() == 0) {
            throw new RuntimeException("Alice has no GAS. If you're running a neo express " +
                    "instance run `neoxp transfer 100 GAS genesis alice` in a terminal in the " +
                    "root directory of this project.");
        }
        AccountSigner signer = AccountSigner.none(deploymentAccount);

        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("${placeholder}", "my string value");

        deployHelloWorldSmartContract(signer, deploymentAccount.getScriptHash(), substitutions,
                neow3j);
    }

    private static Hash160 deployHelloWorldSmartContract(AccountSigner signer, Hash160 owner,
            Map<String, String> substitutions, Neow3j neow3j) throws Throwable {

        CompilationUnit res = new Compiler().compile(
                HelloWorldSmartContract.class.getCanonicalName(),
                substitutions);

        TransactionBuilder builder = new ContractManagement(neow3j)
                .deploy(res.getNefFile(), res.getManifest(), hash160(owner))
                .signers(signer);

        Hash256 txHash = builder.sign().send().getSendRawTransaction().getHash();
        System.out.println("Deployment Transaction Hash: " + txHash.toString());
        Await.waitUntilTransactionIsExecuted(txHash, neow3j);

        NeoApplicationLog log = neow3j.getApplicationLog(txHash).send().getApplicationLog();
        if (log.getExecutions().get(0).getState().equals(NeoVMStateType.FAULT)) {
            throw new Exception("Failed to deploy contract. NeoVM error message: "
                    + log.getExecutions().get(0).getException());
        }

        Hash160 contractHash = SmartContract.calcContractHash(signer.getScriptHash(),
                res.getNefFile().getCheckSumAsInteger(), res.getManifest().getName());
        System.out.println("Contract Hash: " + contractHash);
        return contractHash;
    }
}
