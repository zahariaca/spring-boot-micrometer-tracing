package com.zahariaca.service2.config;

import com.zahariaca.service2.services.Service1Client;
import feign.Feign;
import feign.Logger;
import feign.micrometer.MicrometerCapability;
import feign.micrometer.MicrometerObservationCapability;
import feign.slf4j.Slf4jLogger;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class Config {
    @Bean
    public Service1Client service2Client(
            ObservationRegistry observationRegistry,
            MeterRegistry meterRegistry) {
        return Feign.builder()
                .logger(new Slf4jLogger(Service1Client.class))
                .logLevel(Logger.Level.FULL)
                .addCapability(new MicrometerObservationCapability(observationRegistry))
                .addCapability(new MicrometerCapability(meterRegistry))
                .target(Service1Client.class, "http://127.0.0.1:8080");
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter
                = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA: ");
        return filter;
    }

    @Bean
    public RestClient restTemplate() {
        return RestClient.builder().build();
    }
}
