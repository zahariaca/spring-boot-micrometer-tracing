package com.zahariaca.service2.controllers;

import com.zahariaca.service2.services.Service1Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@Slf4j
@RestController
@RequestMapping("/second")
@RequiredArgsConstructor
public class SecondController {
    RestClient webClient = RestClient.builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .build();

    RestClient serviceOneClient = RestClient.builder()
            .baseUrl("http://localhost:8080")
            .build();

    private final RestClient restClient;

    private final Service1Client service1Client;
    @GetMapping
    public String getSecond() {
        log.info("Call on second controller endpoint.");
//        observationRegistry.getCurrentObservation().highCardinalityKeyValue("test-key", "test-sample");

        return webClient.get()
                .uri("/todos/1")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/first")
    public String getFirst() {
        log.info("Call to first service endpoint.");
        return restClient.get()
                .uri("http://127.0.0.1:8080/first")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/cloud-gateway")
    public String getThroughCloudGateway() {
        log.info("Call to SecondController - getThroughCloudGateway");
        return webClient.get()
                .uri("/todos/1")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/feign-to-first")
    public String getThroughFeign(){
        log.info("Call to FirstController - getThroughFeign");
        return service1Client.getFirst();
    }

}