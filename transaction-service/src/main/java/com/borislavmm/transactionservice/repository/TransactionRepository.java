package com.borislavmm.transactionservice.repository;

import com.borislavmm.transactionservice.model.WalletTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<WalletTransaction, String> {
    List<WalletTransaction> findTop10ByWalletSenderOrWalletReceiverOrderByTimeDesc(String s1, String s2);
}
