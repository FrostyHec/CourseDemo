package org.frosty.test_common.handler;

import org.frosty.infra.initalizer.DatabaseInitializer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

public class DatabaseIdempotencyHandler implements BeforeEachCallback, AfterEachCallback {
    @Autowired
    private DatabaseInitializer databaseInitializer;

//    public boolean annotationExists(ExtensionContext context){
//        if (context.getTestClass().isPresent()){
//            return false;
//        }
//        Class<?> testClass = context.getTestClass().get();
//        return Arrays.stream(testClass.getAnnotations())
//                .anyMatch(annotation -> annotation.annotationType().isAnnotationPresent(DatabaseIdempotency.class) ||
//                        annotation.annotationType().equals(DatabaseIdempotency.class));
//    }

    public void clearDatabase(ExtensionContext context) throws IOException {
        SpringExtension.getApplicationContext(context)
                .getAutowireCapableBeanFactory()
                .autowireBean(this);

        databaseInitializer.init();
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        clearDatabase(context);
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        clearDatabase(context);
    }
}
