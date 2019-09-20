# [File Transfer System (FTS)](https://github.com/jonatascbarroso/file-transfer-system/)

File Transfer System is a distributed solution for transferring and processing files efficiently.

Briefly, the system receives a file, saves in object storage and notifies a messaging topic to be processed.
After upload, the file is read, processed, and stored to stay available for download.
In the end, users use a service to get this processed file.

## Architecture

![Architecture](docs/file-transfer-system-architecture.png)

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

### Prerequisites

* Maven 3
* Java 8

## Running

### Administration Server and Service Discovery

```
cd admin
mvn clean package spring-boot:run
```

