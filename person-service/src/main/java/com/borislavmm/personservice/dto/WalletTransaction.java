package com.borislavmm.personservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WalletTransaction {
    private LocalDateTime time;
    private String walletSender;
    private String walletReceiver;
    private String description;
    private String currencyCode;
    private Double amount;
}