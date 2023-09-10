package com.borislavmm.transactionservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("transactions")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WalletTransaction {
    @Id
    private String id;
    private LocalDateTime time;
    private String walletSender;
    private String walletReceiver;
    private String description;
    private String currencyCode;
    private Double amount;

    public WalletTransaction(LocalDateTime time, String walletSender, String walletReceiver,
                             String description, String currencyCode, double amount) {
        this.time = time;
        this.walletSender = walletSender;
        this.walletReceiver = walletReceiver;
        this.description = description;
        this.currencyCode = currencyCode;
        this.amount = amount;
    }
}