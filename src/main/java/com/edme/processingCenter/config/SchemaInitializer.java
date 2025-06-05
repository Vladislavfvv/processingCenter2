package com.edme.processingCenter.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchemaInitializer {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void createSchemaIfNotExists() {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS processingcenterschema");
        //   jdbcTemplate.execute("GRANT ALL PRIVILEGES ON SCHEMA processingcenterschema TO lesson;");
//        jdbcTemplate.execute("SET search_path TO processingcenterschema");
    }
}
