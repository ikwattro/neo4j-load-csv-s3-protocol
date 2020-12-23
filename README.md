# Neo4j LOAD CSV S3 Protocol

This extension allows to load csv files specifying their location with the `s3` protocol.

Example : 

```
LOAD CSV WITH HEADERS FROM "s3://<access-key>:<access-secret>@<bucket>.s3.amazon.com/<objectname>
```


## How-To

Build this package : 

```
mvn clean package
```

Drop the jar in Neo4j plugins directory

Restart Neo4j