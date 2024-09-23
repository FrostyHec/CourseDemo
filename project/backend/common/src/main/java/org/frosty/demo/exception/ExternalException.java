package org.frosty.demo.exception;




import org.frosty.demo.response.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ExternalException extends RuntimeException{
    private Response response;
}
