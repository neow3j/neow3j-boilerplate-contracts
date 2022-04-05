# neow3j-boilerplate-contracts

This is a boilerplate project setup for Java smart contracts. It brings the minimum amount of code
to compile and test a "Hello World" smart contract for Neo N3.

## Quickstart

[Java 8](https://adoptium.net/) (or higher) is required.  
[Docker](https://www.docker.com/products/docker-desktop) is required for running smart contract
tests.

#### 1. **Clone this git repo:**

```bash
git clone https://github.com/neow3j/neow3j-boilerplate-contracts.git
```

#### 2. **Go to the project directory:**

```bash
cd neow3j-boilerplate-contracts
```

#### 3. **Compile the smart contract:**

```bash
./gradlew neow3jCompile
```

#### 4. **You will see the following output in the directory `./build/neow3j`:**

```bash
$ ls -la build/neow3j 
total 24
drwxr-xr-x  5 user  wheel  160 23 Feb 17:40 .
drwxr-xr-x  7 user  wheel  224 23 Feb 17:40 ..
-rw-r--r--  1 user  wheel  425 23 Feb 17:40 HelloWorldSmartContract.manifest.json
-rw-r--r--  1 user  wheel   94 23 Feb 17:40 HelloWorldSmartContract.nef
-rw-r--r--  1 user  wheel  430 23 Feb 17:40 HelloWorldSmartContract.nefdbgnfo
```

#### 5. **Run the contract test**

```bash
./gradlew test
```

#### 6. **Deploy the contract**

- Run a local [Neo Express](https://github.com/neo-project/neo-express) instance. The project 
  includes a Neo Express configuration file.
- Fund Alice's account: `neoxp transfer 100 GAS genesis alice`
- Go to the `com.axlabs.boilerplate.Deployment` class and run it.

#### 7. **Give us a GitHub star! :star::star::star:**

## About

Neow3j is a Java SDK and smart contract devpack that provides easy and reliable tools to build Neo
dApps and Smart Contracts using the Java platform (Java, Kotlin, Android).

Check out [neow3j.io](https://neow3j.io) for more information on neow3j and the technical
documentation.

Neow3j is an open-source project developed by the community and maintained by
[AxLabs](https://axlabs.com).
