package org.frosty.auth.exception;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
