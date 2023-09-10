package com.borislavmm.exchangerateservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExchangePairResponse {
    @JsonProperty("base_code")
    private String base;
    @JsonProperty("target_code")
    private String target;
    @JsonProperty("conversion_rate")
    private double exchangeRate;
    @JsonProperty("conversion_result")
    private double exchangeResult;
}
