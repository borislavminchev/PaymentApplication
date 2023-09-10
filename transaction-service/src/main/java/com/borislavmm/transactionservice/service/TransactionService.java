package com.borislavmm.transactionservice.service;

import com.borislavmm.transactionservice.dto.TransactionRequest;
import com.borislavmm.transactionservice.model.WalletTransaction;
import com.borislavmm.transactionservice.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public String saveTransaction(LocalDateTime dateTime, TransactionRequest request) {
        WalletTransaction transaction = WalletTransaction.builder()
                        .time(dateTime)
                        .walletSender(request.getWalletSender())
                        .walletReceiver(request.getWalletReceiver())
                        .description(request.getDescription())
                        .currencyCode(request.getCurrencyCode())
                        .amount(request.getAmount())
                        .build();
        transactionRepository.save(transaction);
        return "Success";
    }

    public List<WalletTransaction> getLastTenTransactions(String walletNumber) {

        return transactionRepository.findTop10ByWalletSenderOrWalletReceiverOrderByTimeDesc(walletNumber, walletNumber);
    }
}
