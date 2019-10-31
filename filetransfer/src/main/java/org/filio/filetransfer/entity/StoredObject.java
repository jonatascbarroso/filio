package org.filio.filetransfer.entity;

import java.io.InputStream;
import java.util.Date;

import com.google.gson.Gson;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "objects")
public class StoredObject {

    private String id;

    private String name;

    private Date createdTime;

    @Transient
    private InputStream content;

    private String contentType;

    private long length;

    public String json() {
        return new Gson().toJson(this);
    }

}