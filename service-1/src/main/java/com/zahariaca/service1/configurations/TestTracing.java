package com.zahariaca.service1.configurations;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TestTracing implements WebFilter {
    private final ObservationRegistry observationRegistry;
    private final Tracer tracer;

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        var headers = exchange.getRequest().getHeaders();
        tracer.currentSpan()
                        .tag("X-TRACE-ID", headers.get("X-TRACE-ID").get(0));
        tracer.createBaggageInScope("X-TRACE-ID", headers.get("X-TRACE-ID").get(0));
        return chain.filter(exchange)
                .contextWrite(context -> {
                    System.out.println("AZ -------------------------");
                    return context.put("X-TRACE-ID", "12345");
                });
    }
}
