package org.filio.exception;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class encapsulates the error response.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private String[] errors;

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, null);
    }

    public static ErrorResponse of(int status, String message, String...errors) {
        return new ErrorResponse(status, message, errors);
    }

    public static ErrorResponse of(int status, String message, Collection<String> errors) {
        return new ErrorResponse(status, message, errors != null ? errors.toArray(new String[] {}) : null);
    }

}