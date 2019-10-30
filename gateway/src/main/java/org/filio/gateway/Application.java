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

	@Value("${services.fileTransfer.url}")
	private String FILE_TRANSFER_SERVICE_URL;
	
	@Value("${services.fileTransfer.resourceName}")
	private String FILE_TRANSFER_RESOURCE_NAME;

	String FILE_TRANSFER_CIRCUIT_BREAKER_NAME = "fileTransferCircuitBreaker";
	String FILE_TRANSFER_SERVICE_FALLBACK_URI = "fallbackFileTransferService";

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder.routes()
			
			.route("download", r -> r.method(HttpMethod.GET)
				.and()
				.path("/" + FILE_TRANSFER_RESOURCE_NAME + "/{id}")
				.filters(f -> f.hystrix(h -> h.setName(FILE_TRANSFER_CIRCUIT_BREAKER_NAME)
					.setFallbackUri("forward:/" + FILE_TRANSFER_SERVICE_FALLBACK_URI)))
				.uri(FILE_TRANSFER_SERVICE_URL))
			
			.route("upload", r -> r.method(HttpMethod.POST)
				.and()
				.path("/" + FILE_TRANSFER_RESOURCE_NAME)
				.filters(f -> f.hystrix(h -> h.setName(FILE_TRANSFER_CIRCUIT_BREAKER_NAME)
					.setFallbackUri("forward:/" + FILE_TRANSFER_SERVICE_FALLBACK_URI)))
				.uri(FILE_TRANSFER_SERVICE_URL))

		.build();
	}
	
}
