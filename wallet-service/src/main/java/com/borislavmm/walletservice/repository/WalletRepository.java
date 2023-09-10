package com.borislavmm.walletservice.repository;

import com.borislavmm.walletservice.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @Query(value = "select * from wallet w where w.wallet_number = ?1", nativeQuery = true)
    Optional<Wallet> getWalletByNumber(String walletNumber);
}
