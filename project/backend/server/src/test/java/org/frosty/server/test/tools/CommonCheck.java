package org.frosty.server.test.tools;

import java.util.List;

public class CommonCheck {

    public static <T> T checkAndGetOne(List<T> li){
        assert li.size()==1;
        return li.get(0);
    }
}
