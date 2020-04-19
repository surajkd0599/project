package com.ttn.bootcamp.project.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class EcommerceApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(EcommerceApplication.class, args);
    }
}
