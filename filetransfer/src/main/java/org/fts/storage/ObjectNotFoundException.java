package org.fts.storage;

/**
 * This exception is used to indicate when
 * an employee is looked up but not found.
 */
public class ObjectNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5110423958223833545L;

    public ObjectNotFoundException(String id) {
        super("Could not find object " + id);
    }

}