package com.borislavmm.walletservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferRequest {
    private String senderWallet;
    private String receiverWallet;
    private String currencyCodeSend;
    private String currencyCodeReceive;
    private String description;
    private double amount;
}
