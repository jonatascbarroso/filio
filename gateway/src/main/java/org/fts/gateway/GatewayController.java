package org.fts.gateway;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GatewayController {
    
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/")
    @HystrixCommand(fallbackMethod = "fallbackFileTransferService")
    public String getCallFileTransferInstance() {
        String fileTransferServiceUrl = "http://file-transfer/";
        String response = restTemplate.exchange(fileTransferServiceUrl,
            HttpMethod.GET, null, new ParameterizedTypeReference<String>() {},
            String.class).getBody();
        return "File Transfer Service Instance: " + response;
    }

    public String fallbackFileTransferService() {
        String message = "Fallback response: File Transfer Service is not available";
        log.error(message);
        return message;
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}