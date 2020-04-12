package org.filio.orderManager;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(value="/")
@Slf4j
public class Controller {

    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    @GetMapping
    public ResponseEntity<String> get() {
        log.debug("Returning service instance.");
        String instanceId = new String("Service Instance: " + this.instanceId);
        return ResponseEntity.ok().body(instanceId);
    }
}