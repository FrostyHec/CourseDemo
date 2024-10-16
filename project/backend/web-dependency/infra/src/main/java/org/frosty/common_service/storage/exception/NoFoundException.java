package org.frosty.common_service.storage.exception;

public class NoFoundException extends Exception {

    public NoFoundException(String message) {
        super(message);
    }

    public NoFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
