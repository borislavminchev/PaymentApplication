package com.borislavmm.walletmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewWalletRequest {
    private String walletNumber;
    private String initialCurrencyCode;
}
