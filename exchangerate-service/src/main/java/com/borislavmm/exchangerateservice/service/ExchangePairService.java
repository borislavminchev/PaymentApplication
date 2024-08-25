package com.borislavmm.exchangerateservice.service;


import com.borislavmm.exchangerateservice.dto.ExchangePairResponse;
import com.borislavmm.exchangerateservice.hashes.ExchangePair;
import com.borislavmm.exchangerateservice.repository.ExchangeRateRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ExchangePairService {
    @Value("${api.key}")
    private String API_KEY;
    @Value("${api.endpoint}")
    private String API_ENDPOINT;

    private final WebClient.Builder webClient;
    private final ExchangeRateRedisRepository repository;

    public ExchangePairResponse getExchangeRate(String base, String target, double amount) {
        Optional<ExchangePair> pair = repository.findById(base + ":" + target);
        if(pair.isPresent()) {
            ExchangePair result = pair.get();
            return ExchangePairResponse.builder()
                    .base(result.getFromCurrency())
                    .target(result.getToCurrency())
                    .exchangeRate(result.getRate())
                    .exchangeResult(amount * result.getRate())
                    .build();
        }

        ExchangePairResponse apiResponse = getExchangeRateAPI(base, target, amount);
        repository.save(new ExchangePair(apiResponse.getBase(),
                apiResponse.getTarget(), apiResponse.getExchangeRate()));

        return apiResponse;
    }

    public ExchangePairResponse getExchangeRateAPI(String base, String target, double amount) {
        String uri = String.format("%s/%s/pair/%s/%s%s",
                API_ENDPOINT, API_KEY, base, target, (amount==0?"":"/"+amount));

        return webClient.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(ExchangePairResponse.class)
                .block();
    }
}
