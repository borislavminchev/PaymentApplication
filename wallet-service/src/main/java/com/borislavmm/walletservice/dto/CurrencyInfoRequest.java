package com.borislavmm.walletservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyInfoRequest {
    private String walletNumber;
    private String currencyCode;
}
