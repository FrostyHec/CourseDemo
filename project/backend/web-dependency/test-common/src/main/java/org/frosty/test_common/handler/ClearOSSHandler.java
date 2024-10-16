package org.frosty.test_common.handler;

import org.frosty.common.exception.InternalException;
import org.frosty.common_service.storage.api.ObjectStorageService;
import org.frosty.common_service.storage.api.impl.MockObjectStorageServiceImpl;
import org.frosty.common_service.storage.api.impl.ObjectStorageServiceImpl;
import org.frosty.infra.initalizer.MinioInitializer;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class ClearOSSHandler implements BeforeEachCallback, AfterEachCallback {
    @Autowired // it is a bad check, may use other way instead
    private ObjectStorageService objectStorageService;
    @Autowired
    private MinioInitializer minioInitializer;

    public void clear(ExtensionContext context) throws Exception {
        SpringExtension.getApplicationContext(context)
                .getAutowireCapableBeanFactory()
                .autowireBean(this);
        if (objectStorageService instanceof ObjectStorageServiceImpl) {
            minioInitializer.init();
        } else if (objectStorageService instanceof MockObjectStorageServiceImpl) {
            ((MockObjectStorageServiceImpl) objectStorageService).clearAll();
        } else {
            throw new InternalException("Unknown ObjectStorageService: " + objectStorageService.getClass().getName());
        }
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
