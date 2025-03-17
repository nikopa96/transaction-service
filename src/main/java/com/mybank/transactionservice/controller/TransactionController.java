package com.mybank.transactionservice.controller;

import com.mybank.transactionservice.api.TransactionApi;
import com.mybank.transactionservice.api.model.TransactionRequest;
import com.mybank.transactionservice.api.model.TransactionResponse;
import com.mybank.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    public ResponseEntity<TransactionResponse> debitMoneyFromBankAccount(TransactionRequest request) {
        return ResponseEntity.ok(transactionService.debitMoneyFromBankAccount(request));
    }

    @Override
    public ResponseEntity<TransactionResponse> depositMoneyToBankAccount(TransactionRequest request) {
        return ResponseEntity.ok(transactionService.depositMoneyToBankAccount(request));
    }
}
