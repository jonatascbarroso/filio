package org.filio.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
public class Application {

	@Value("${loadBalancer.fileTransfer.url}")
    private String FILE_TRANSFER_SERVICE_URL;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes()
		.route("download", r -> r.path("/files/{id}")
			.and()
			.method(HttpMethod.GET)
			.filters(f -> f.hystrix(h -> h.setName("fileTransferCircuitBreaker")
				.setFallbackUri("forward:/fallbackFileTransferService")))
			.uri(FILE_TRANSFER_SERVICE_URL))
		.route("upload", r -> r.path("/files")
			.and()
			.method(HttpMethod.POST)
			.filters(f -> f.hystrix(h -> h.setName("fileTransferCircuitBreaker")
				.setFallbackUri("forward:/fallbackFileTransferService")))
			.uri(FILE_TRANSFER_SERVICE_URL))
		.build();
	}
	
}
