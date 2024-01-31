package com.zahariaca.service1.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FirstService {
    private final WebClient webClient;

    public Mono<String> contactSecondService() {
        log.info("Inside FirstService - contactSecondService");
        return webClient.get()
                .uri("http://127.0.0.1:8081/second")
                .retrieve()
                .bodyToMono(String.class);
    }

}
