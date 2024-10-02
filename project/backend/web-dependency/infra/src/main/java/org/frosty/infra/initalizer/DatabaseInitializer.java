package org.frosty.infra.initalizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DatabaseInitializer implements Initializer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void init() throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:database/*.sql");
        for (Resource resource : resources) {
            try (InputStream inputStream = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                StringBuilder scriptBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    scriptBuilder.append(line).append(System.lineSeparator());
                }
                String script = scriptBuilder.toString();
                jdbcTemplate.execute(script);
            }
        }
    }
}
