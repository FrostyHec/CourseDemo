package org.frosty.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.frosty.common.response.Response;

@AllArgsConstructor
@Getter
@Setter
public class ExternalException extends RuntimeException{
    private Response response;
}
