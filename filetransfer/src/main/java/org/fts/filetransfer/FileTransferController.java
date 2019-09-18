package org.fts.filetransfer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
public class FileTransferController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileTransferController.class);

    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    /**
     * Is geared to handle multi-part message file
     * and give it to the Storage Service for saving.
     * @param file multipart file
     * @return the file id
     */
    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        LOGGER.debug("Uploading file to service instance " + this.instanceId);
        String response = null;
        // todo
        return ResponseEntity.ok().body(response);
    }

    /**
     * 
     * @param id the file id
     * @return the file status
     */
    @GetMapping("status/{id:.+}")
    @ResponseBody
    public ResponseEntity<String> status(@PathVariable String id) {
        LOGGER.debug("Getting the situation of file " + id + " by using the service instance " + this.instanceId);
        String response = null;
        // todo
        return ResponseEntity.ok().body(response);
    }

    /**
     * Loads the resource if it exists, and sends it to the client
     * to download using a "Content-Disposition" response header.
     * @param id the file id
     * @return the file content
     */
    @GetMapping("files/{id:.+}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String id) {
        LOGGER.debug("Downloading file " + id + " on service instance " + this.instanceId);
        Resource file = null;
        // todo
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}