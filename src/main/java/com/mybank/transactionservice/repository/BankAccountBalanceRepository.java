package com.mybank.transactionservice.repository;

import com.mybank.transactionservice.entity.BankAccountBalanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BankAccountBalanceRepository extends JpaRepository<BankAccountBalanceEntity, UUID> {

    Optional<BankAccountBalanceEntity> findByUuid(UUID uuid);
}
