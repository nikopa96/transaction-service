package com.mybank.transactionservice.service;

import com.mybank.transactionservice.api.model.FailedTransactionReason;
import com.mybank.transactionservice.api.model.TransactionRequest;
import com.mybank.transactionservice.api.model.TransactionResponse;
import com.mybank.transactionservice.api.model.TransactionType;
import com.mybank.transactionservice.entity.BankAccountBalanceEntity;
import com.mybank.transactionservice.exception.TransactionException;
import com.mybank.transactionservice.repository.BankAccountBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final BankAccountBalanceRepository bankAccountBalanceRepository;

    public TransactionResponse debitMoneyFromBankAccount(TransactionRequest request) {
        BankAccountBalanceEntity bankAccountBalance = getBankAccountBalance(request);

        BigInteger currentBalance = bankAccountBalance.getBalance();
        BigInteger amountToBeSubtracted = BigDecimal.valueOf(request.getAmount())
                .movePointRight(2)
                .toBigIntegerExact();
        BigInteger newBalance = currentBalance.subtract(amountToBeSubtracted);

        if (newBalance.compareTo(BigInteger.ZERO) < 0) {
            throw new TransactionException("Unable to debit money. Not enough money in the account.",
                    FailedTransactionReason.NOT_ENOUGH_MONEY);
        }

        bankAccountBalance.setBalance(newBalance);
        bankAccountBalanceRepository.save(bankAccountBalance);

        return TransactionResponse.builder()
                .type(TransactionType.DEBIT)
                .success(true)
                .build();
    }

    public TransactionResponse depositMoneyToBankAccount(TransactionRequest request) {
        BankAccountBalanceEntity bankAccountBalance = getBankAccountBalance(request);

        BigInteger currentBalance = bankAccountBalance.getBalance();
        BigInteger amountToBeAdded = BigDecimal.valueOf(request.getAmount())
                .movePointRight(2)
                .toBigIntegerExact();
        BigInteger newBalance = currentBalance.add(amountToBeAdded);

        bankAccountBalance.setBalance(newBalance);
        bankAccountBalanceRepository.save(bankAccountBalance);

        return TransactionResponse.builder()
                .type(TransactionType.DEPOSIT)
                .success(true)
                .build();
    }

    private BankAccountBalanceEntity getBankAccountBalance(TransactionRequest request) {
        Optional<BankAccountBalanceEntity> bankAccountBalanceOpt = bankAccountBalanceRepository
                .findByUuid(request.getBankAccountBalanceUuid());

        return bankAccountBalanceOpt
                .orElseThrow(() -> new TransactionException("Unable to find a bank account balance by UUID.",
                        FailedTransactionReason.ACCOUNT_BALANCE_NOT_FOUND));
    }
}
