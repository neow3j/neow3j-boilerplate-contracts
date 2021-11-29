package org.example;

import io.neow3j.devpack.ByteString;
import io.neow3j.devpack.Hash160;
import io.neow3j.devpack.Storage;
import io.neow3j.devpack.StorageContext;
import io.neow3j.devpack.annotations.DisplayName;
import io.neow3j.devpack.annotations.ManifestExtra;
import io.neow3j.devpack.annotations.OnDeployment;

@DisplayName("HelloWorld")
@ManifestExtra(key = "author", value = "Your Name")
public class HelloWorldSmartContract {

    static final String OWNER_KEY = "owner";
    static StorageContext ctx = Storage.getStorageContext();

    static String aString = "${change_this}";

    @OnDeployment
    public static void deploy(Object data, boolean update) {
        if (!update) {
            Storage.put(ctx, OWNER_KEY, (Hash160) data);
        }
    }

    public static ByteString getOwner() {
        return Storage.get(ctx, OWNER_KEY);
    }

    public static String getAString() {
        return aString;
    }
}
