package org.filio.filetransfer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.gson.Gson;

import org.filio.exception.ObjectNotFoundException;
import org.filio.exception.ServiceException;
import org.filio.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
     * Is geared to handle multi-part message file and give it to the Storage
     * Service for saving.
     * 
     * @param file multiparted
     * @return the file id
     * @throws IOException
     */
    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        log.debug("Uploading file to service instance " + this.instanceId);
        InputStream content = new BufferedInputStream(file.getInputStream());
        String id = storage.putObject(content);
        content.close();
        String response = new Gson().toJson(id);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Loads the resource if it exists, and sends it to the client
     * to download using a "Content-Disposition" response header.
     * 
     * @param id of the file
     * @return the file content
     */
    @GetMapping("files/{id}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String id) {
        log.debug("Downloading file " + id + " on service instance " + this.instanceId);
        InputStreamResource resource = new InputStreamResource(storage.getObject(id));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<?> handlesObjectNotFound(ObjectNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handlesServiceError(ServiceException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handlesServiceError(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    
}