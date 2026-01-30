package com.tansen.government.core;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
public class DbConfig {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void checkDb() throws Exception {
        System.out.println("DB URL = " + dataSource.getConnection().getMetaData().getURL());
        System.out.println("DB USER = " + dataSource.getConnection().getMetaData().getUserName());
    }
}
