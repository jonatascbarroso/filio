package org.fts.filetransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
class FileTransferApplication {

	static void main(String[] args) {
		SpringApplication.run(FileTransferApplication.class, args);
	}

}
