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

#### Web Server & Reverse Proxy



#### Gateways

Components to manage and protect the public API of the backend.
* **Metadata Gateway**
* **File Tranfer Gateway**

#### Services

* **Order Manager**
* **File Manager**
* **Processor**
* **Validator**
* **Notificator**

#### Message Topics

* **Processing**
* **Validation**
* **Notification**

#### Util Services

* **Admin Server** is a management service of the all system.
* **Config Server** is responsible for managing externalized configuration in a distributed system.
* **Discovery Server** is a registration and discovery service of the system microservices.

#### Object Storage

Container to stock temporary and processed files by the system.

#### Webhook



### Technologies

* Front-end: [React](https://reactjs.org/)
* Back-end (microservices): [Spring Boot](https://spring.io/projects/spring-boot)
* Messaging: [Apache Kafka](https://kafka.apache.org/)
* Object Storage: [MinIO](https://min.io/)
* Database: [MongoDB](https://www.mongodb.com)

### Prerequisites

* Maven 3
* Java 11
* MinIO
* MongoDB

## Running

### Manually and Locally

#### Spring Boot Applications (SBA)

```sh
$ cd {app-dir}
$ mvn clean package spring-boot:run
```

#### Cloud Config

It is necessary to run the Config Server first so that other applications can obtain the updated settings.
Config files are in `{filio-repository}\config\{application}\{profile}\` folder.
To refresh app configurations, you need to run the following command:

```sh
$ curl -X GET http://{user}:{password}@localhost:{port}/actuator/refresh
```

#### MinIO

1. Download MinIO.
2. Start MinIO server.

```sh
$ minio server .
```

#### MongoDB

1. Download and install MongoDB.
2. Create a folder to put your data: `mongodb-data`.
3. Start MongoDB server.

```sh
$ mongod --port 27017 --dbpath ./mongodb-data
```

4. Connect to server.

```sh
$ mongo localhost:27017
```

5. Create an administrator user.

```
> use admin
> db.createUser({user: "root", pwd: "root", roles:["root"]})
```

6. Restart MongoDB server and enable authentication with `--auth` flag.

```sh
$ mongod --auth --port 27017 --dbpath ./mongodb-data
```

7. Connect to it as administrator.

```sh
$ mongo localhost:27017 -u "root" -p "root" --authenticationDatabase "admin"
```

8. Create a app user to `filio` db.

```
> use filio
> db.createUser({user: "user", pwd: "password", roles:["dbOwner"]})
```

9. Reconnect with new user credentials.

```sh
$ mongo localhost:27017/filio -u "user" -p "password"
```

