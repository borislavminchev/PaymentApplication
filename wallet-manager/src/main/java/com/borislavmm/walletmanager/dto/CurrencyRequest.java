package com.borislavmm.walletmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRequest {
    private String currencyCode;
    private double amount;
    private String walletNumber;
}
