package com.borislavmm.walletservice.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private String walletSender;
    private String walletReceiver;
    private String description;
    private String currencyCode;
    private double amount;
}
