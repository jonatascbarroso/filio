package org.filio.filetransfer;

import org.filio.storage.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class FileTransferController {

    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    @Autowired
    private ObjectStorageService storage;

    /**
     * Is geared to handle multi-part message file
     * and give it to the Storage Service for saving.
     * @param file multiparted
     * @return the file id
     */
    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        log.debug("Uploading file to service instance " + this.instanceId);
        String response = null;
        // todo
        return ResponseEntity.ok().body(response);
    }

    /**
     * Loads the resource if it exists, and sends it to the client
     * to download using a "Content-Disposition" response header.
     * @param id of the file
     * @return the file content
     */
    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String id) {
        log.debug("Downloading file " + id + " on service instance " + this.instanceId);
        Resource file = null;
        // todo
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }
    
}