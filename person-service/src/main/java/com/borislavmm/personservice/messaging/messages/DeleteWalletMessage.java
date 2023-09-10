package com.borislavmm.personservice.messaging.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteWalletMessage {

    private String walletNumber;
}
