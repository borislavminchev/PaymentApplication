package com.borislavmm.exchangerateservice.hashes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "exchangeRate", timeToLive = 3600*24)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangePair {
    @Id
    private String id;
    private String fromCurrency;
    private String toCurrency;
    private Double rate;

    public ExchangePair(String fromCurrency, String toCurrency, Double rate) {
        this.id = fromCurrency + ":" + toCurrency;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.rate = rate;
    }
}
