package org.frosty.common.utils;

import org.frosty.common.exception.InternalException;
import org.frosty.common.response.Response;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class RestTemplateUtils {
    public static Object checkSuccess(ResponseEntity<Response> res,String onHTTPCodeFailed,String onResponseCodeFailed){
        Ex.check(res.getStatusCode().is2xxSuccessful(),new InternalException(onHTTPCodeFailed+"\n HTTP Response:"+res));
        Response body =res.getBody();
        Ex.check(Objects.requireNonNull(body).getCode()==Response.getSuccess().getCode(),
                new InternalException(onResponseCodeFailed+"Response:"+res));
        return res.getBody();
    }
}
