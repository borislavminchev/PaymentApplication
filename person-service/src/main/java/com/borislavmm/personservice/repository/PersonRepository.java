package com.borislavmm.personservice.repository;

import com.borislavmm.personservice.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<List<Person>> findByWallet(String walletNumber);
}
