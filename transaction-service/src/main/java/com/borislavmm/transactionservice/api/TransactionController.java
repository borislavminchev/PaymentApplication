package com.borislavmm.transactionservice.api;

import com.borislavmm.transactionservice.model.WalletTransaction;
import com.borislavmm.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/last-ten/{number}")
    public CompletableFuture<List<WalletTransaction>> getLastTen(@PathVariable String number) {
        return CompletableFuture.supplyAsync( () -> transactionService.getLastTenTransactions(number));
    }
}
