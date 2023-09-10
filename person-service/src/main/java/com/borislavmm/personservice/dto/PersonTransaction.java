package com.borislavmm.personservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonTransaction {
    private long senderId;
    private long receiverId;
    private String description;
    private String currencyCode;
    private double amount;
}
