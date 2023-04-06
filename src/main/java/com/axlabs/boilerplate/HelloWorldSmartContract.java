package com.axlabs.boilerplate;

import io.neow3j.devpack.ByteString;
import io.neow3j.devpack.Storage;
import io.neow3j.devpack.annotations.DisplayName;
import io.neow3j.devpack.annotations.ManifestExtra;
import io.neow3j.devpack.annotations.OnDeployment;

import static io.neow3j.devpack.Storage.getReadOnlyContext;
import static io.neow3j.devpack.Storage.getStorageContext;
import static io.neow3j.devpack.StringLiteralHelper.addressToScriptHash;

@DisplayName("HelloWorld")
@ManifestExtra(key = "author", value = "Your Name")
public class HelloWorldSmartContract {

    static final byte[] ownerKey = new byte[]{0x00};

    static final String staticValue = "Hello World!";

    @OnDeployment
    public static void deploy(Object data, boolean update) {
        Storage.put(getStorageContext(), ownerKey, addressToScriptHash("NNSyinBZAr8HMhjj95MfkKD1PY7YWoDweR"));
    }

    public static ByteString getOwner() {
        return Storage.get(getReadOnlyContext(), ownerKey);
    }

    public static String getStaticValue() {
        return staticValue;
    }

}
