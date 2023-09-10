package com.borislavmm.walletservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "currency_information")
public final class CurrencyInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="currency_code")
    private String currencyCode;

    @Column(name="available_amount")
    private double availableAmount;

    @Column(name="frozen_amount")
    private double frozenAmount;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "wallet", nullable = false)
    private Wallet wallet;

    public CurrencyInformation(String currencyCode, double availableAmount, double frozenAmount) {
        this.currencyCode = currencyCode;
        this.availableAmount = availableAmount;
        this.frozenAmount = frozenAmount;
    }


    public void addAvailableAmount(double amount) {
        this.availableAmount+=amount;
    }

    public void takeAvailableAmount(double amount) {
        if(availableAmount < amount) throw new IllegalArgumentException("Doesn't have enough money in " + currencyCode);

        availableAmount -= amount;
    }
}
