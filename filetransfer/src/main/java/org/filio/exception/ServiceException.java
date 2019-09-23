package org.filio.exception;

/**
 * This exception is used to indicate when an error
 * occurred while trying to access or use a service.
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceException(String service) {
        super("Could not access or use " + service);
    }

}