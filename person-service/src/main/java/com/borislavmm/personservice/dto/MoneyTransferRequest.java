package com.borislavmm.personservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferRequest {
    private String senderWallet;
    private String receiverWallet;
    private String currencyCodeSend;
    private String currencyCodeReceive;
    private double amount;
}
