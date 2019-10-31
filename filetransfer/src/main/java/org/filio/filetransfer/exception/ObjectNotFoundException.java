package org.filio.filetransfer.exception;

/**
 * This exception is used to indicate when
 * an object is looked up but not found.
 */
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(String id) {
        super("Could not find object " + id);
    }

}