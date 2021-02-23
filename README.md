# neow3j-boilerplate

This is a boilerplate for a neow3j project. It brings the minimum amount of code to compile your first "hello world" Neo3 smart contract.

:rotating_light: Make sure you have Java 1.8 (or higher) installed. :rotating_light:

Follow these **deadly** easy steps:

* Clone this git repo:

```
git clone https://github.com/neow3j/neow3j-boilerplate.git
```

* Go to the project directory:

```
cd neow3j-boilerplate
```

* Compile:

```
./gradlew neow3jCompile
```

* You will see the output in `./build/neow3j` directory:

```
$ ls -la build/neow3j 
total 24
drwxr-xr-x  5 user  wheel  160 23 Feb 17:40 .
drwxr-xr-x  7 user  wheel  224 23 Feb 17:40 ..
-rw-r--r--  1 user  wheel  425 23 Feb 17:40 HelloWorldSmartContract.manifest.json
-rw-r--r--  1 user  wheel   94 23 Feb 17:40 HelloWorldSmartContract.nef
-rw-r--r--  1 user  wheel  430 23 Feb 17:40 HelloWorldSmartContract.nefdbgnfo
```

* Yayyyy! :rocket: :tada:
