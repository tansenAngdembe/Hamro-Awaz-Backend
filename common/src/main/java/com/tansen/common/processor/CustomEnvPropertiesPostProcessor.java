package com.tansen.common.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class CustomEnvPropertiesPostProcessor implements EnvironmentPostProcessor {
//    It allows you to change or add configuration values before Spring loads beans and auto-configuration.
    private static final String PROPERTY_SOURCE_NAME = "customEnvPropertiesPostProcessor";
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> envProperties = new HashMap<>();

        try {
            DataSource dataSource = DataSourceBuilder.create()
                    .url(environment.getProperty("spring.datasource.url"))
                    .username(environment.getProperty("spring.datasource.username"))
                    .password(environment.getProperty("spring.datasource.password"))
                    .driverClassName(environment.getProperty("spring.datasource.driver-class-name"))
                    .build();

            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(
                         "SELECT `key`, `value` FROM application_config")
            ) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String key = rs.getString("key");
                    String value = rs.getString("value");
                    envProperties.put(key, value);
                }
            }

            environment.getPropertySources()
                    .addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, envProperties));

        } catch (Exception e) {
            throw new RuntimeException("Failed to load environment properties from DB", e);
        }
    }
}
