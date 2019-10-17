package org.filio.filetransfer.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * This error handler intercept exceptions
 * from controllers accross the application.
 */
@RestControllerAdvice
public class ControllerErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleObjectNotFoundException(final ObjectNotFoundException exception, final WebRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return handleExceptionInternal(exception, ErrorResponse.of(httpStatus.value(),
            exception.getMessage()), new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleServiceException(final ServiceException exception, final WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_GATEWAY;
        return handleExceptionInternal(exception, ErrorResponse.of(httpStatus.value(),
            exception.getMessage()), new HttpHeaders(), httpStatus, request);
    }

}