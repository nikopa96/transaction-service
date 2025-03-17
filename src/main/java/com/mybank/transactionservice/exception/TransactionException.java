package com.mybank.transactionservice.exception;

import com.mybank.transactionservice.api.model.FailedTransactionReason;
import lombok.Getter;

@Getter
public class TransactionException extends RuntimeException {

    private final FailedTransactionReason reason;

    public TransactionException(String message, FailedTransactionReason reason) {
        super(message);
        this.reason = reason;
    }
}
