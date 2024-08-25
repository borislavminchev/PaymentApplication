package com.borislavmm.exchangerateservice.repository;

import com.borislavmm.exchangerateservice.hashes.ExchangePair;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRedisRepository extends CrudRepository<ExchangePair, String> {
}
