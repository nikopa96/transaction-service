package com.mybank.transactionservice.exception;

import com.mybank.transactionservice.api.model.FailedOnlinePaymentReason;
import lombok.Getter;

@Getter
public class OnlinePaymentException extends RuntimeException {

    private final FailedOnlinePaymentReason reason;

    public OnlinePaymentException(String message, FailedOnlinePaymentReason reason) {
        super(message);
        this.reason = reason;
    }
}
