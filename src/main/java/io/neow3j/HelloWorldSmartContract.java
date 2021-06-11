package io.neow3j;

import io.neow3j.devpack.annotations.ManifestExtra;
import io.neow3j.devpack.annotations.Safe;

// A simple smart contract with one method that returns a string and takes no arguments.
@ManifestExtra(key = "name", value = "HelloWorldSmartContract")
public class HelloWorldSmartContract {

    @Safe
    public static String bongoCat() {
        return "neowwwwwwwwww";
    }

}
