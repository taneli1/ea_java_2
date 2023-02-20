package com.example.ea_java_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EaJava2Application {

    public static void main(String[] args) {
        SpringApplication.run(EaJava2Application.class, args);
    }

}
