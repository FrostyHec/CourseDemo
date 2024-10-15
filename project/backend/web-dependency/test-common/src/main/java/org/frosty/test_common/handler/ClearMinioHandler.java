package org.frosty.test_common.handler;

import lombok.RequiredArgsConstructor;
import org.frosty.infra.initalizer.DatabaseInitializer;
import org.frosty.infra.initalizer.MinioInitializer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class ClearMinioHandler implements BeforeEachCallback, AfterEachCallback {

    @Autowired
    private MinioInitializer minioInitializer;

    public void clear(ExtensionContext context) throws Exception {
        SpringExtension.getApplicationContext(context)
                .getAutowireCapableBeanFactory()
                .autowireBean(this);
        minioInitializer.init();
    }
    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        clear(extensionContext);
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {
        clear(extensionContext);
    }
}
