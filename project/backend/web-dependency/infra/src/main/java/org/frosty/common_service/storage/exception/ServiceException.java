package org.frosty.common_service.storage.exception;

public class ServiceException extends Exception {
    // complex exception occurs in server side, not due to bad params

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
