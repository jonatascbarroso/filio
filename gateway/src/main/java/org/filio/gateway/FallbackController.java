package org.filio.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FallbackController {

    @Value("${services.fileTransfer.name}")
    private String FILE_TRANSFER_SERVICE_NAME;

    @RequestMapping("/fallbackFileTransferService")
    public Mono<String> fallbackFileTransferService() {
        String message = createFallbackMessage(FILE_TRANSFER_SERVICE_NAME);
        log.error(message);
        return Mono.just(message);
    }

    private String createFallbackMessage(String serviceName) {
        return "Fallback response: " + serviceName + " Service is not available.";
    }

}