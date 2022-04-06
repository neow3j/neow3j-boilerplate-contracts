package com.axlabs.boilerplate;

import io.neow3j.devpack.Contract;
import io.neow3j.devpack.Hash160;
import io.neow3j.devpack.Runtime;
import io.neow3j.devpack.Storage;
import io.neow3j.devpack.annotations.DisplayName;
import io.neow3j.devpack.annotations.ManifestExtra;
import io.neow3j.devpack.annotations.OnDeployment;
import io.neow3j.devpack.annotations.OnNEP17Payment;
import io.neow3j.devpack.annotations.OnVerification;
import io.neow3j.devpack.annotations.Safe;
import io.neow3j.devpack.constants.CallFlags;
import io.neow3j.devpack.contracts.GasToken;
import io.neow3j.devpack.events.Event2Args;

@DisplayName("HelloWorld")
@ManifestExtra(key = "author", value = "AxLabs")
public class HelloWorldSmartContract {

    static final String OWNER_KEY = "owner";

    @OnDeployment
    public static void deploy(Object data, boolean update) {
        if (!update) {
            // 1. Set contract owner upon deployment
            Storage.put(Storage.getStorageContext(), OWNER_KEY, (Hash160) data);
        }
    }

    // 2. Get contract owner
    @Safe
    public static Hash160 getContractOwner() {
        return new Hash160(Storage.get(Storage.getReadOnlyContext(), OWNER_KEY));
    }

    // 3. Change contract owner
    public static void changeContractOwner(Hash160 newContractOwner) throws Exception {
        if (!Hash160.isValid(newContractOwner)) {
            throw new Exception("Invalid Hash160 value provided.");
        }
        if (!Runtime.checkWitness(getContractOwner())) {
            throw new Exception("No authorization.");
        }
        Storage.put(Storage.getStorageContext(), OWNER_KEY, newContractOwner);
    }

    // 4. Receive (only) GAS
    @OnNEP17Payment
    public static void receiveFunds(Hash160 from, int amount, Object data) throws Exception {
        if (data != null) {
            throw new Exception("No data allowed.");
        }
        if (from == null || !Hash160.isValid(from) || from.isZero()) {
            throw new Exception("Invalid sender provided.");
        }
        if (Runtime.getCallingScriptHash() != GasToken.getHash()) {
            throw new Exception("Only GAS accepted.");
        }
        onPayment.fire(from, amount);
    }

    // 5. Throw an event 'onGasPayment(from, amount)' when receiving funds
    @DisplayName("onGasPayment")
    public static Event2Args<Hash160, Integer> onPayment;

    // 6. Withdraw GAS (verify withdrawal transaction)
    @OnVerification
    public static boolean verify() {
        return Runtime.checkWitness(getContractOwner());
    }

    // 7. Call another contract
    @Safe
    public static int getContractGasBalance() {
        return (int) Contract.call(GasToken.getHash(), "balanceOf", CallFlags.ReadStates,
                new Object[]{Runtime.getExecutingScriptHash()});
    }

    @Safe
    public static int getContractGasBalanceWithWrapper() {
        return GasToken.balanceOf(Runtime.getExecutingScriptHash());
    }

}
