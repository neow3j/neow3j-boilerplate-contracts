package com.axlabs.boilerplate;

import io.neow3j.compiler.CompilationUnit;
import io.neow3j.compiler.Compiler;
import io.neow3j.contract.ContractManagement;
import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.response.NeoApplicationLog;
import io.neow3j.protocol.core.response.NeoSendRawTransaction;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.AccountSigner;
import io.neow3j.transaction.Transaction;
import io.neow3j.types.Hash160;
import io.neow3j.types.Hash256;
import io.neow3j.types.NeoVMStateType;
import io.neow3j.utils.Await;
import io.neow3j.wallet.Account;

import static java.lang.String.format;

public class Deployment {

    private static final String ALICE_WIF = "KzrHihgvHGpF9urkSbrbRcgrxSuVhpDWkSfWvSg97pJ5YgbdHKCQ";
    private static final Account OWNER_ACCOUNT = Account.fromWIF(ALICE_WIF);
    private static final String NODE = "http://localhost:50012";

    public static void main(String[] args) throws Throwable {
        Neow3j neow3j = Neow3j.build(new HttpService(NODE));

        // Compile the HelloWorldSmartContract
        CompilationUnit res = new Compiler().compile(HelloWorldSmartContract.class.getCanonicalName());

        // Build the deployment transaction
        AccountSigner signer = AccountSigner.none(OWNER_ACCOUNT);
        Transaction transaction = new ContractManagement(neow3j)
                .deploy(res.getNefFile(), res.getManifest())
                .signers(signer)
                .sign();

        // Send the transaction to the node
        NeoSendRawTransaction response = transaction.send();
        if (response.hasError()) {
            throw new Exception("Sent transaction resulted in an error: " + response.getError().getMessage());
        }

        Hash256 txHash = response.getResult().getHash();
        System.out.printf("Deployment transaction hash: '%s'\n", txHash);
        Await.waitUntilTransactionIsExecuted(txHash, neow3j);

        NeoApplicationLog log = neow3j.getApplicationLog(txHash).send().getApplicationLog();
        if (log.getExecutions().get(0).getState().equals(NeoVMStateType.FAULT)) {
            // Alice might not have any GAS. If you're running a neo express instance run the following command in a
            // terminal in the root directory of this project:
            // $ neoxp transfer 100 GAS genesis alice

            throw new Exception(format("Failed to deploy contract. NeoVM error message: %s",
                     log.getExecutions().get(0).getException()));
        }

        // Calculate the contract hash from the deployment parameters and the deployer account.
        // This could also be read from the notification thrown in the transaction.
        Hash160 contractHash = SmartContract.calcContractHash(
                signer.getScriptHash(),
                res.getNefFile().getCheckSumAsInteger(),
                res.getManifest().getName()
        );
        System.out.printf("Contract hash: '%s'\n", contractHash);
    }

}
