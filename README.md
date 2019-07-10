# SOEN 6441 Battleship

### Installing Maven (Mac Only)
```shell
brew install maven
```

## Import Project in IntelliJ
`Open > pom.xml > Open as Project`

 Run:
 ```shell
 mvn install
 ```


## Run the project (direct)
```shell
mvn exec:java -Dexec.mainClass="com.soen6441.battleship.app.App"
```

## Run the project (compile)

- Clean and build jar.
```shell
mvn clean package
```
- Run jar file.
```shell
java -cp target/soen6441-battleship-1.0-SNAPSHOT.jar com.soen6441.battleship.app.App
```