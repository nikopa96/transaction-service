package com.mybank.transactionservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigInteger;
import java.util.UUID;

@Entity
@Table(name = "bank_account_balance")
@Data
public class BankAccountBalanceEntity {

    @Id
    private UUID uuid;

    private String currencyCode;

    private BigInteger balance;
}
