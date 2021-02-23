package io.neow3j;

import io.neow3j.devpack.annotations.ManifestExtra;

// A simple smart contract with one method 
// that returns a string and takes no arguments.
@ManifestExtra(key = "name", value = "HelloWorldSmartContract")
public class HelloWorldSmartContract {
    
    public static String bongoCat() {
        return "neowwwwwwwwww";
    }
    
}