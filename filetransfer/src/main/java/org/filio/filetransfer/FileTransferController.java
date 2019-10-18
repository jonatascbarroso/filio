package org.filio.filetransfer;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import com.google.gson.Gson;

import org.filio.filetransfer.exception.ObjectNotFoundException;
import org.filio.filetransfer.exception.ServiceException;
import org.filio.filetransfer.service.implementation.MinIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.codec.multipart.Part;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "/api/files")
@Slf4j
public class FileTransferController {

    @Value("${eureka.instance.instanceId}")
    private String instanceId;

    @Autowired
    private MinIOService storage;

    /**
     * Is geared to handle multi-part file and give it to the
     * Storage Service for saving.
     * 
     * @param parts of a flux providing all part contained in the request
     * @return a flux of results - the file id
     */
    @RequestMapping(method = RequestMethod.POST,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> upload(@RequestBody Flux<Part> parts) {
        log.debug("Uploading file to service instance " + this.instanceId);
        return parts
            .filter(part -> part instanceof FilePart)
            .ofType(FilePart.class)
            .flatMap(this::saveFile);
    }

    private Mono<String> saveFile(FilePart part) {
        return DataBufferUtils.join(part.content()).map(dataBuffer -> {
            String id = UUID.randomUUID().toString();
            InputStream content = dataBuffer.asInputStream();
            storage.putObject(id, content);
            try {
                content.close();
            } catch (IOException e) {
            }
            return new Gson().toJson(id);
        });
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