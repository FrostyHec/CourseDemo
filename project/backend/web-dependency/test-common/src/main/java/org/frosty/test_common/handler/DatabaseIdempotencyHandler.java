package org.frosty.test_common.handler;

import org.frosty.infra.initalizer.DatabaseInitializer;
import org.frosty.test_common.annotation.DatabaseIdempotency;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DatabaseIdempotencyHandler implements BeforeEachCallback, AfterEachCallback {
    @Autowired
    private DatabaseInitializer databaseInitializer;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (context.getTestClass().isPresent() &&
                context.getTestClass().get().isAnnotationPresent(DatabaseIdempotency.class)) {

            SpringExtension.getApplicationContext(context)
                    .getAutowireCapableBeanFactory()
                    .autowireBean(this);

            databaseInitializer.init();
        }
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (context.getTestClass().isPresent() &&
                context.getTestClass().get().isAnnotationPresent(DatabaseIdempotency.class)) {

            SpringExtension.getApplicationContext(context)
                    .getAutowireCapableBeanFactory()
                    .autowireBean(this);

            databaseInitializer.init();
        }
    }
}
