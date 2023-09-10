package com.borislavmm.walletservice.repository;

import com.borislavmm.walletservice.model.CurrencyInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyInformationRepository extends JpaRepository<CurrencyInformation, Long> {
}
