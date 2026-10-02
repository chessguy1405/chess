# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

[API Endpoint Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoFmipzGsvz-BwVygYKQH+iMykoKp+h-Ds0KPMB4lUEiMAIEJ4rTuWKkwGpOykm+hi7jS+4MkyU76YZWwuTenl3kuwowGKEpujKcplu8Sr+cZAJsKo8SYCqwYagAkmgVAmkg676TA0BOUZ6lBbyIUWVZPZ9tu7kWf62W5cgHCCcJUYxnGhRaUmlSiWmTgAIwETmqh5vM0FFiW9Q+NMl7QEgABeKC7HRTbDg6vUdpZLobu6W5uYKG30jAh5yCgz5XtoGKXdex0CqF9SPgGl6vs69WVDppZteKGSqABmBfSB1S6YRDnzCRqHfBRVH1pDtHofCybIKmMC4fhoxg3FxGkdDl6w8h8NofRjHeH4-heCg6AxHEiSU9TbW+FgomCqB9QNNIEb8RG7QRt0PRyaoCnDDDiHoIj2lmbpotIaZsIYVVu02fYTPnvjYtoK571Hbe-LeWAl1q-BGtzsFm2VMu4Xik+r3aLK8oy+L6XqjATV5eujuFPdwOdt2vb9odW1s67OXuwTHUoLGCkS1t-UwOmw2Y6N40FmMU3QDNc16gty2rY2DHe0H71Pbb8h1TrZsnRwKDcMepfADd9emxV5tCvUGQzBANAvS+B3a1tX31Izp5-QDQNFxJYGaSDSN9Sj2Fo3hWZrQxnhkwEKLrv42Dihq-FojAADiSoaCzDWlg0h88-z9hKiL6tITHn1S2Bnty1+rPF9ZaLHzmRuUSbcuHkW4nUZAbS8-8CbNwXA9C2YUIo217vIe2xoH5O1VFlUOLVUHG0foXRWXYYA1QDv3c+9Q3YtXDtGSOXUY7IxKGAAaidsz8hTpNYsGcFRZ3iDnFaDYSb4N9F-W6fdOwVxAaOGAYDf5qAxNA-c954HWyPkqD0gjtpWRkW9MRA8X71BkfvHIgMX6f0ni8MYt8cwFgaOMSxKBMrSALINcIwRAggk2PEXUKA3Scj2N8ZIoA1Q+Mgosb4diABySpQkXBgHQueDCcJL0xnY1Q1jbFKgcU4lxbjlgeK8cEz4oSQQBJAEEoiE0xhhKVJEuY0SYCdBXqTZi-gOAAHY3BOBQE4GIEZghwC4gANngBOQwMjYlnyEWYxorQOg3zvtwgmWYIlKmnpPSW8tX5oLQGsZZcx37mUmTtQhaAUCbBkZAjWOzqlKi1joqkutQFMkNp7eRIU4H1AQT3K6yCYqezShgkOzV8o4IAXgh5D0CF+1qoHMhgKw4awjlHeMPV6GowTiNVh+Z2HTS4RRXhecBHgp9kckuSDgBAPuZXSRJyzlKgxFo7QrzW6W2egysu6i3L6NUaIo57ZB4wHCacmRo8ECARMRPGo5i7GZPqM41xqzEyooXujAi0rHGyuyY0tezTLA1xspsGmSAEhgF1X2CABqABSEBxQqLmDEEpaoijzzEocySTRmQyR6HY++uD0BZmwAgYAuqoBwAgDZKAVy5gOIVQrZ+GyXie1UoG4Nobw2RvsdIfZsaNG7QAFbWrQOcxNTlk2UFTdAdNDjbm8uATA-WzytlMsXO8q2EoRE-Idls-5GU4XYL+Ryr+xCgGwooflKhnVo4oviWioaGLcxYsLBw0ss08VQCWnwxpA7fZfO0TWylEj9bnLVU22BbdW3rjZcAFBaryowOJZo7lZdnYagDUGstYbiomjNDWcM3VSFxq-DNcpxiNnEv9OWqAYYkKItoVO+AzqBqZiTpiiai6cVfuDOaGAtZ6yNMDvuutMADBgHOZek9ijXTYC0OiW1KBoryhkQ44dAG4RQv7OPWFIwY2z3gwkxeGMuP5yaeTLwQbDXGtE-KRAwZYDAGwAGwgeQCjjOdaYyVjRObc15vzYwT8HjxpgIJ8ehyrIgG4HgHkciKUyHBfUMzMmZGqCs4XFtHcu6GBNAgWju7BwEa8qdczUA3ROfI49GAbnu6eb2moolYWIseb7DurcW6SWGYVYOD8ei0sgY-hK3S3HMIIf48vITQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
