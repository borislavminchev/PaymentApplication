package com.borislavmm.walletservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRequest {
    private String walletNumber;
    private String baseCode;
    private String targetCode;
    private double amount;
    private double exchangeRate;
}
