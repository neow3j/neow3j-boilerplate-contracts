# neow3j-boilerplate

This is a boilerplate for a neow3j project. It brings the minimum amount of code to compile your first `hello world` smart contract for Neo N3.

## Quickstart

> Make sure you have [Java 1.8](https://adoptopenjdk.net/installation.html) (or higher) installed.

Follow these **deadly** easy steps:

1. **Clone this git repo:**

```bash
git clone https://github.com/neow3j/neow3j-boilerplate.git
```

2. **Go to the project directory:**

```bash
cd neow3j-boilerplate
```

3. **Compile the smart contract:**

```bash
./gradlew neow3jCompile
```

4. **You will see the following output in the directory `./build/neow3j`:**

```bash
$ ls -la build/neow3j 
total 24
drwxr-xr-x  5 user  wheel  160 23 Feb 17:40 .
drwxr-xr-x  7 user  wheel  224 23 Feb 17:40 ..
-rw-r--r--  1 user  wheel  425 23 Feb 17:40 HelloWorldSmartContract.manifest.json
-rw-r--r--  1 user  wheel   94 23 Feb 17:40 HelloWorldSmartContract.nef
-rw-r--r--  1 user  wheel  430 23 Feb 17:40 HelloWorldSmartContract.nefdbgnfo
```

5. **Yayyyy!** :rocket: :tada:

6. **Give us a GitHub star! :star::star::star:**

## About

Neow3j is a development toolkit that provides easy and reliable tools to build Neo dApps and Smart
Contracts using the Java platform (Java, Kotlin, Android).

It is an open-source project developed by the community and maintained by
[AxLabs](https://axlabs.com).

Check out [neow3j.io](https://neow3j.io) for more information on neow3j and the technical documentation.
