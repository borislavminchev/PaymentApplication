package com.borislavmm.walletservice.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePersonMessage {
    private String personWalletNumber;
}
