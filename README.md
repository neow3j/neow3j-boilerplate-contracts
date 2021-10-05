# neow3j-boilerplate

This is a boilerplate project setup for Java smart contracts. It brings the minimum amount of code
to compile and test a "Hello World" smart contract for Neo N3.

## Quickstart

> [Java 1.8](https://adoptopenjdk.net/installation.html) (or higher) is required.
> [Docker](https://www.docker.com/products/docker-desktop) is required for running smart contract 
  tests.

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

5. **Run the contract test**

```bash
./gradlew test
```

6. **Give us a GitHub star! :star::star::star:**

## Adapting the Boilerplate Code

To adapt the boilerplate project to your own smart contract project, make sure to apply the
following changes.

- Project name. I.e., the root folder's name and the `rootProject.name` property in the
  *settings.gradle* file.
- Name of the contract. I.e., the name of the class and the value in the `@DisplayName`
  annotation on the contract class.
- Name of the author. I.e., the value in the `@ManifestExtra(key = "author", value = "Your Name")`
  annotation on the contract class.
- Package name.
- Group name. I.e., the `group 'org.example'` property in the *build.gradle* file.
- Neow3j Gradle plugin property `className = "org.example.HelloWorldSmartContract"` in the
  *build.gradle* file according to the new package and class name you chose.

## About

Neow3j is a development toolkit that provides easy and reliable tools to build Neo dApps and Smart
Contracts using the Java platform (Java, Kotlin, Android).

It is an open-source project developed by the community and maintained by
[AxLabs](https://axlabs.com).

Check out [neow3j.io](https://neow3j.io) for more information on neow3j and the technical documentation.
