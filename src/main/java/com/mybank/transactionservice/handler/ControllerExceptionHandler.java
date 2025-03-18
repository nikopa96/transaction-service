package com.mybank.transactionservice.handler;

import com.mybank.transactionservice.api.model.FailedOnlinePaymentResponse;
import com.mybank.transactionservice.api.model.FailedTransactionResponse;
import com.mybank.transactionservice.exception.OnlinePaymentException;
import com.mybank.transactionservice.exception.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<FailedTransactionResponse> handleTransactionException(TransactionException exception) {
        FailedTransactionResponse responseBody = FailedTransactionResponse
                .builder()
                .failureReason(exception.getReason())
                .success(false)
                .build();

        return new ResponseEntity<>(responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(OnlinePaymentException.class)
    public ResponseEntity<FailedOnlinePaymentResponse> handleOnlinePaymentException(OnlinePaymentException exception) {
        FailedOnlinePaymentResponse responseBody = FailedOnlinePaymentResponse
                .builder()
                .failureReason(exception.getReason())
                .success(false)
                .build();

        return new ResponseEntity<>(responseBody, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
