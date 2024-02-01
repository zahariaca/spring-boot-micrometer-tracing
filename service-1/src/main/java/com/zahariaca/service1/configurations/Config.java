package com.zahariaca.service1.configurations;

import com.zahariaca.service1.services.Service2Client;
import feign.Feign;
import feign.Logger;
import feign.micrometer.MicrometerCapability;
import feign.micrometer.MicrometerObservationCapability;
import feign.slf4j.Slf4jLogger;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration

public class Config {

    public static final String REQUEST_TRACE_ID = "X-TRACE-ID";

    @Bean
    public Service2Client service2Client(
            ObservationRegistry observationRegistry,
            MeterRegistry meterRegistry) {
        return Feign.builder()
                .logger(new Slf4jLogger(Service2Client.class))
                .logLevel(Logger.Level.FULL)
                .addCapability(new MicrometerObservationCapability(observationRegistry))
                .addCapability(new MicrometerCapability(meterRegistry))
                .target(Service2Client.class, "http://127.0.0.1:8081");
    }

    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder
                .build();
    }
}
