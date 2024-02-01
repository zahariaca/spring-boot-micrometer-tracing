package com.zahariaca.service1.controller;

import com.zahariaca.service1.services.FirstService;
import com.zahariaca.service1.services.Service2Client;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RestController
@RequestMapping("/first")
@RequiredArgsConstructor
public class FirstController {
    private final FirstService firstService;
    private final WebClient webClient;
    private final Service2Client service2Client;
    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;

    @GetMapping
    public Mono<String> getFirst() {
        log.info("Call on first controller endpoint.");
//        observationRegistry.getCurrentObservation().highCardinalityKeyValue("test-key", "test-sample");

        return webClient.get()
                .uri("https://jsonplaceholder.typicode.com/todos/1")
                .retrieve()
                .bodyToMono(String.class);
    }

    @GetMapping("/feignclient")
    public Mono<String> getSecond() {
        log.info("CAll to 2nd microservice via feign");
        return Mono.fromCallable(service2Client::getSecond);
    }

    @GetMapping("/webclient")
    public Mono<String> getWebClientSecond() {
        log.info("Get from 2nd service via webclient");
        tracer.createBaggageInScope("test-baggage", "test-baggage-value");
        return firstService.contactSecondService()
                .contextCapture()
                .subscribeOn(Schedulers.boundedElastic());


    }

}
