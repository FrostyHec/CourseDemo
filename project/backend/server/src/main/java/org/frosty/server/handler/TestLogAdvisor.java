package org.frosty.server.handler;

import org.springframework.beans.factory.annotation.Value;

public class TestLogAdvisor {
    // TODO config by settings
    // 1. exception advisor
    // 2. response-log
    @Value("${logging.with-test}")
    private boolean onTestLog;
}
