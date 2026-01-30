package com.tansen.government;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"com.tansen"})
@EntityScan(basePackages = "com.tansen.entity")
@EnableJpaRepositories(basePackages = "com.tansen.repository")
@EnableAsync
@EnableScheduling
public class GovernmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernmentApplication.class, args);
    }

}
