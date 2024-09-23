package org.frosty.demo.testutils;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.frosty.demo.testutils.mapper.DatabaseManager;


public class EnsureTestStateImmutabilityFilter implements BeforeEachCallback, AfterEachCallback {
    @Autowired
    private DatabaseManager databaseManager;
    @Override
    public void beforeEach(ExtensionContext context) {
        if (databaseManager == null) {
            databaseManager = SpringExtension.getApplicationContext(context).getBean(DatabaseManager.class);
        }
        databaseManager.clearDatabase();
        databaseManager.resetAutoStart();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (databaseManager == null) {
            databaseManager = SpringExtension.getApplicationContext(context).getBean(DatabaseManager.class);
        }
        databaseManager.clearDatabase();
        databaseManager.resetAutoStart();
    }
}
