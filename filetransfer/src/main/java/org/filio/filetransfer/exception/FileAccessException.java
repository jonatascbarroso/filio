package org.filio.filetransfer.exception;

/**
 * This exception is used to indicate when an error
 * occurred while trying to access or use a file.
 */
public class FileAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public FileAccessException() {
        super("Could not access or use file");
    }

}