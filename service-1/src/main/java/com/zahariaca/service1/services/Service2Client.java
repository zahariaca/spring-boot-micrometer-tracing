package com.zahariaca.service1.services;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("service2Client")
public interface Service2Client {
    @RequestLine("GET /second")
    String getSecond();
}
