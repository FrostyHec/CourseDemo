package org.frosty.common.handler;

import lombok.extern.log4j.Log4j2;
import org.frosty.common.constant.AdvisorConstant;
import org.frosty.common.exception.ExternalException;
import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.frosty.common.response.ResponseCodeType;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RestControllerAdvice
@Order(AdvisorConstant.defaultLevel)
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