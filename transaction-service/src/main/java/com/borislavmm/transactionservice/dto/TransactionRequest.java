package com.borislavmm.transactionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String walletSender;
    private String walletReceiver;
    private String description;
    private String currencyCode;
    private double amount;
}
