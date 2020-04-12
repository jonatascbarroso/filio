# [Filio](https://github.com/jonatascbarroso/filio/)

**Filio**, *son* in Latin and *FILe In Out* in English, is a distributed solution for transferring and processing files efficiently.

Briefly, the system receives a file, saves in object storage and notifies a messaging topic to be processed.
After upload, the file is read, processed, and stored to stay available for download.
In the end, users use a service to get this processed file.

## Architecture

![Architecture](docs/filio-architecture.png)

### Components

#### Frontend App

Graphical interface used to send and receive files to the system.

#### Gateway

Component to manage and protect the public API of the backend using load balancer and circuit breaker.

#### Admin Server

Discovery and management service of the system microservices.

#### File Transfer Service

Component with methods to upload, download, check, and get info of the managed files.

#### Incoming Topic

Message repository to indicate to system process an uploaded file.

#### Object Storage

Container to stock temporary and processed files by the system.

#### Processor Service

Topic consumer that processes each message in the Incoming Topic.

#### Logging

Logs all important system events, e.g. API access, microservices communication, errors, etc.

### Technologies

* Front-end: [React](https://reactjs.org/)
* Back-end (microservices): [Spring Boot](https://spring.io/projects/spring-boot)
* Messaging: [Apache Kafka](https://kafka.apache.org/)
* Object Storage: [MinIO](https://min.io/)
* Database: [MongoDB](https://www.mongodb.com)

### Prerequisites

* Maven 3
* Java 8
* MinIO
* MongoDB

## Running

### Manually

#### Spring Boot Apps

```
cd {app-dir}
mvn clean package spring-boot:run
```

#### Cloud Config

Config files could be found in `config\{application}\{profile}\` dir.

To refresh app configurations, you need run the following command:

```
curl -X GET http://{user}:{password}@host:{port}/actuator/refresh
```

#### MinIO

1. Download MinIO.
2. Start MinIO server.
```
minio server .
```

#### MongoDB

1. Download and install MongoDB.
2. Create a folder to put your data: `mongodb-data`.
3. Start MongoDB server.
```
mongod --port 27017 --dbpath ./mongodb-data
```
4. Connect to server.
```
mongo localhost:27017
```
5. Create an administrator user.
```
> use admin
> db.createUser({user: "root", pwd: "root", roles:["root"]})
```
6. Restart MongoDB server and enable authentication with --auth flag.
```
mongod --auth --port 27017 --dbpath ./mongodb-data
```
7. Connect to it as administrator.
```
mongo localhost:27017 -u "root" -p "root" --authenticationDatabase "admin"
```
8. Create a app user to `filio` db.
```
> use filio
> db.createUser({user: "user", pwd: "password", roles:["dbOwner"]})
```
9. Reconnect with new user credentials.
```
mongo localhost:27017/filio -u "user" -p "password"
```

