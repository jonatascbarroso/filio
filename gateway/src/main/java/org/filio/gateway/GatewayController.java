package org.filio.gateway;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class GatewayController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${serviceDiscovery.fileTransfer.name}")
    private String FILE_TRANSFER_SERVICE_NAME;

    @Value("${serviceDiscovery.fileTransfer.url}")
    private String FILE_TRANSFER_SERVICE_URL;

    @GetMapping("/{id}")
    @HystrixCommand(fallbackMethod = "fallbackFileTransferService")
    public String download(@PathVariable String id) {
        String path = FILE_TRANSFER_SERVICE_URL + "/files/{id}";
        Map<String, String> uriVariables = new HashMap();
        uriVariables.put("id", id);
        String response = restTemplate.getForObject(path, InputStream.class, uriVariables);
        return FILE_TRANSFER_SERVICE_NAME + " Instance: " + response;
    }

    public String fallbackFileTransferService() {
        String message = createFallbackMessage(FILE_TRANSFER_SERVICE_NAME);
        log.error(message);
        return message;
    }

    private String createFallbackMessage(String serviceName) {
        return "Fallback response: " + serviceName + " is not available.";
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}