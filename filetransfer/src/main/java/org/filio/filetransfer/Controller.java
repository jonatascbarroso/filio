package org.filio.filetransfer;

import org.filio.filetransfer.entity.StoredObject;
import org.filio.filetransfer.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/files")
@Slf4j
public class Controller {

    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    @Autowired
    private ObjectStorageService storage;

    @PostMapping
    public String upload(@RequestParam("file") MultipartFile file) {
        log.debug("Uploading file to service instance " + this.instanceId);
        return storage.putObject(file);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        log.debug("Downloading file " + id + " on service instance " + this.instanceId);
        StoredObject object = storage.getObject(id);
        InputStreamResource resource = new InputStreamResource(object.getContent());
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + object.getName() + "\"")
            .body(resource);
    }
    
}