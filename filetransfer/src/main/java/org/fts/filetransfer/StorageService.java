package org.fts.filetransfer;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface StorageService {
    
    String send(MultipartFile file);

    Resource receive(String id);

    String metadata(String id);

}