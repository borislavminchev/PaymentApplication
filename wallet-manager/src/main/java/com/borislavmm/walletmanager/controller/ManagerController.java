package com.borislavmm.walletmanager.controller;

import com.borislavmm.walletmanager.dto.CurrencyInfoRequest;
import com.borislavmm.walletmanager.dto.CurrencyRequest;
import com.borislavmm.walletmanager.dto.MoneyTransferRequest;
import com.borislavmm.walletmanager.dto.NewWalletRequest;
import com.borislavmm.walletmanager.service.WalletManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final WalletManagerService managerService;

    @PostMapping("/load-wallet")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> loadWallet(@RequestBody CurrencyRequest currencyRequest) {
        return CompletableFuture.supplyAsync(() -> managerService.loadWallet(currencyRequest));
    }

    @PostMapping("/take-money")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> takeMoney(@RequestBody CurrencyRequest currencyRequest) {
        return CompletableFuture.supplyAsync(() -> managerService.takeMoney(currencyRequest));
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> transferMoney(@RequestBody MoneyTransferRequest transferRequest) {
        return CompletableFuture.supplyAsync(() -> managerService.transferMoney(transferRequest.getSenderWallet(), transferRequest.getReceiverWallet(),
                transferRequest.getCurrencyCodeSend(), transferRequest.getCurrencyCodeReceive(), transferRequest.getAmount()));
    }

    @PostMapping("/new-wallet")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> createNewWallet(@RequestBody NewWalletRequest newWalletRequest) {
        return CompletableFuture.supplyAsync( () -> managerService.createWallet(newWalletRequest.getWalletNumber(),
                newWalletRequest.getInitialCurrencyCode()));
    }

    @PostMapping("/delete-currency")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> removeCurrency(@RequestBody CurrencyInfoRequest currencyInfoRequest) {
        return CompletableFuture.supplyAsync(() -> managerService.deleteCurrency(currencyInfoRequest));
    }

    @PostMapping("/delete-wallet")
    @ResponseStatus(HttpStatus.OK)
    public CompletableFuture<String> deleteWallet(@RequestBody String walletNumber) {
        return CompletableFuture.supplyAsync(() -> managerService.deleteWallet(walletNumber));
    }


}
