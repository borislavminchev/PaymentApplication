package com.borislavmm.walletservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "wallet_number")
    private String walletNumber;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    private List<CurrencyInformation> currencies;

    public Wallet(String walletNumber) {
        this.walletNumber = walletNumber;
        currencies = new ArrayList<>();
    }

    public void addCurrency(CurrencyInformation currency) {
        if(!currencies.contains(currency)) {
            currencies.add(currency);
            currency.setWallet(this);
        }
    }
}
