package org.fts.storage;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

 /**
  * When an ObjectNotFoundException is thrown,
  * this configuration is used to render an HTTP 404.
  */
@ControllerAdvice
public class ObjectNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String objectNotFoundHandler(ObjectNotFoundException e) {
        return e.getMessage();
    }

}