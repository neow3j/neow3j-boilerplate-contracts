package com.axlabs.boilerplate;

import io.neow3j.devpack.ByteString;
import io.neow3j.devpack.Hash160;
import io.neow3j.devpack.Storage;
import io.neow3j.devpack.annotations.DisplayName;
import io.neow3j.devpack.annotations.ManifestExtra;
import io.neow3j.devpack.annotations.OnDeployment;

@DisplayName("HelloWorld")
@ManifestExtra(key = "author", value = "Your Name")
public class HelloWorldSmartContract {

    static final byte[] ownerKey = new byte[]{0x00};

    static String staticValue = "${placeholder}";

    @OnDeployment
    public static void deploy(Object data, boolean update) {
        if (!update) {
            Storage.put(Storage.getStorageContext(), ownerKey, (Hash160) data);
        }
    }

    public static ByteString getOwner() {
        return Storage.get(Storage.getReadOnlyContext(), ownerKey);
    }

    public static String getStaticValue() {
        return staticValue;
    }

}
