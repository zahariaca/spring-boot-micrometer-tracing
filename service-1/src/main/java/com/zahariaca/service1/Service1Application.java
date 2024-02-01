package com.zahariaca.service1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactor.core.publisher.Hooks;

@SpringBootApplication
public class Service1Application {

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        SpringApplication.run(Service1Application.class, args);
    }

}
