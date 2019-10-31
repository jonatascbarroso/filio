package org.filio.loadbalancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FallbackController {

    @Value("${service.name}")
    private String SERVICE_NAME;

    @RequestMapping("/fallback")
    public Mono<String> fallbackFileTransfer() {
        String message = createFallbackMessage(SERVICE_NAME);
        log.error(message);
        return Mono.just(message);
    }

    private String createFallbackMessage(String serviceName) {
        return "Fallback response: " + serviceName + " Services are not available.";
    }

}