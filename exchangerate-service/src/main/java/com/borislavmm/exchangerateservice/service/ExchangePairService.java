package com.borislavmm.exchangerateservice.service;


import com.borislavmm.exchangerateservice.dto.ExchangePairResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class ExchangePairService {
    @Value("${api.key}")
    private String API_KEY;
    @Value("${api.endpoint}")
    private String API_ENDPOINT;

    public ExchangePairResponse getExchangeRate(String base, String target, double amount) {
        String uri = String.format("%s/%s/pair/%s/%s%s", API_ENDPOINT, API_KEY, base, target, (amount==0?"":"/"+amount));

        return WebClient.builder().build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ExchangePairResponse.class)
                .block();
    }
}
