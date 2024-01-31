package com.zahariaca.service2.services;

import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("service1Client")
public interface Service1Client {
    @RequestLine("GET /first")
    String getFirst();
}
