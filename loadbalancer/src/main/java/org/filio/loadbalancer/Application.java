package org.filio.loadbalancer;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableEurekaClient
@RestController
@Slf4j
public class Application {

	@Autowired
    private RestTemplate restTemplate;

    @Value("${service.url}")
    private String SERVICE_URL;
    
    @Value("${service.resourceUri}")
    private String SERVICE_RESOURCE_URI;

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
    }
    
    @GetMapping("/files/{id}")
    @HystrixCommand(fallbackMethod = "fallback")
    public Resource download(@PathVariable String id) {
        log.debug("Requesting file " + id + " from the load balancer");
        String url = SERVICE_URL + SERVICE_RESOURCE_URI + "{id}";
        return restTemplate.getForObject(url, Resource.class, id);
    }

	@Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
