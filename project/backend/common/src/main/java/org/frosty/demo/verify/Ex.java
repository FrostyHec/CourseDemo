package org.frosty.demo.verify;

import org.frosty.demo.exception.ExternalException;
import org.frosty.demo.response.Response;

public class Ex {

    private Ex() {
    }

    public static void check(boolean condition, Response response) {
        Ex.check(condition, new ExternalException(response));
    }

    public static void check(boolean condition, RuntimeException e){
        if (!condition) {
            throw e;
        }
    }
}
