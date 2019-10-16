package org.filio.entity;

import java.io.InputStream;
import java.util.Date;

import com.google.gson.Gson;

import lombok.Data;

@Data
public class StoredObject {

    private String id;

    private InputStream content;

    private Date createdTime;

    private long length;

    public String json() {
        return new Gson().toJson(this);
    }

}