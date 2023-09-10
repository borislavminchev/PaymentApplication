package com.borislavmm.walletservice.controller;

import com.borislavmm.walletservice.dto.CurrencyInfoRequest;
import com.borislavmm.walletservice.dto.CurrencyRequest;
import com.borislavmm.walletservice.dto.ExchangeRequest;
import com.borislavmm.walletservice.dto.MoneyTransferRequest;
import com.borislavmm.walletservice.model.CurrencyInformation;
import com.borislavmm.walletservice.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService service;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> addMoneyToWallet(@RequestBody CurrencyRequest currencyRequest) {
        return CompletableFuture.supplyAsync(() -> service.addMoney(currencyRequest.getWalletNumber(),
                currencyRequest.getCurrencyCode(),
                currencyRequest.getAmount()));
    }

    @PostMapping("/take")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> takeMoneyFromWallet(@RequestBody CurrencyRequest currencyRequest) {
        return CompletableFuture.supplyAsync(() -> service.takeMoney(currencyRequest.getWalletNumber(),
                        currencyRequest.getCurrencyCode(),
                        currencyRequest.getAmount()));
    }

    @PostMapping("/exchange")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> exchangeMoney(@RequestBody ExchangeRequest exchangeRequest) {
        return CompletableFuture.supplyAsync(() -> service.exchangeMoney(exchangeRequest.getWalletNumber(),
                        exchangeRequest.getBaseCode(), exchangeRequest.getTargetCode(),
                        exchangeRequest.getExchangeRate(), exchangeRequest.getAmount()));
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest) {
        return CompletableFuture.supplyAsync(() -> service.transferMoney(transferRequest.getSenderWallet(), transferRequest.getReceiverWallet(),
                transferRequest.getCurrencyCodeSend(), transferRequest.getCurrencyCodeReceive(), transferRequest.getAmount()));
    }

    @PostMapping("/delete-currency")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> removeCurrencyInformation(@RequestBody CurrencyInfoRequest currencyInfoRequest) {
        return CompletableFuture.supplyAsync(() -> service.removeCurrencyInformation(currencyInfoRequest.getWalletNumber(),
                currencyInfoRequest.getCurrencyCode()));
    }

    @PostMapping("/delete-wallet")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> removeWallet(@RequestBody String walletNumber) {
        return CompletableFuture.supplyAsync(() -> service.removeWallet(walletNumber));
    }
}
