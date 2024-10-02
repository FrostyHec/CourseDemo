package org.frosty.common.verify;

import org.frosty.common.exception.ExternalException;
import org.frosty.common.response.Response;

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
