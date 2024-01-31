package com.zahariaca.service1.filters;


import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebExchangeDecorator;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.System.lineSeparator;

@Slf4j
@Component
@Order(0)
public class LogRequestFilter implements WebFilter {

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
        if (log.isDebugEnabled()) {
            var capture = new BodyCaptureExchange(exchange);
            return chain.filter(capture)
                    .doOnSuccess(se -> log.debug(capture.getRequest().log(true)))
                    .doOnError(throwable -> log.debug(capture.getRequest().log(false)));
        }
        return chain.filter(exchange);
    }

    static class BodyCaptureRequest extends ServerHttpRequestDecorator {
        private final StringBuilder collector = new StringBuilder(64);

        public BodyCaptureRequest(final ServerHttpRequest delegate) {
            super(delegate);
            collector.append(" ---> ")
                    .append(delegate.getMethod())
                    .append(" \"")
                    .append(delegate.getURI())
                    .append("\" | headers: [ ");
            // @formatter:off
            delegate.getHeaders()
                    .forEach((k, v) -> collector
                            .append('"')
                            .append(k)
                            .append(": ")
                            .append(String.join(";", v))
                            .append("\" ")
                    );
            collector.append("] ");
            // @formatter:on
            if (!HttpMethod.GET.equals(delegate.getMethod())) {
                collector.append("| body: ");
            }
        }

        @Override
        public Flux<DataBuffer> getBody() {
            return super.getBody().doOnNext(this::capture);
        }

        private void capture(final DataBuffer buffer) {
            this.collector.append(StandardCharsets.UTF_8.decode(buffer.toByteBuffer()));
        }

        public String log(final boolean succeeded) {
            return "succeeded:" + succeeded + " req:" + this.collector.append(lineSeparator()).append("<----");
        }
    }

    static class BodyCaptureExchange extends ServerWebExchangeDecorator {
        private final BodyCaptureRequest bodyCaptureRequest;

        public BodyCaptureExchange(final ServerWebExchange exchange) {
            super(exchange);
            this.bodyCaptureRequest = new BodyCaptureRequest(exchange.getRequest());
        }

        @Override
        public BodyCaptureRequest getRequest() {
            return bodyCaptureRequest;
        }
    }
}
