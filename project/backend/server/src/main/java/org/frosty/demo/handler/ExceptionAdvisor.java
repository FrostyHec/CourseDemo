package org.frosty.demo.handler;

import org.frosty.demo.exception.ExternalException;
import org.frosty.demo.response.Response;
import org.frosty.demo.response.ResponseCodeType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import org.frosty.demo.exception.InternalException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(InternalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exceptionHandler(InternalException e) {
        log.error("InternalException:", e);
        return new Response(ResponseCodeType.INTERNAL_ERROR,
                            e.getMessage(),
                            e.getCause());
    }

    @ExceptionHandler(ExternalException.class)
    @ResponseStatus(HttpStatus.OK)
    public Response exceptionHandler(ExternalException e) {
        log.warn("ExternalException:" + e.getResponse());
        return e.getResponse();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exceptionHandler(Exception e) {
        log.error("InternalException:", e);
        return new Response(ResponseCodeType.INTERNAL_ERROR,
                            e.getMessage(),
                            e.getCause());
    }
}