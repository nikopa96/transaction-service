package com.mybank.transactionservice.service;

import com.mybank.transactionservice.api.model.*;
import com.mybank.transactionservice.dto.PaymentGatewayAck;
import com.mybank.transactionservice.exception.OnlinePaymentException;
import com.mybank.transactionservice.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class OnlinePaymentService {

    private final TransactionService transactionService;
    private final RestClient restClient;

    public OnlinePaymentResponse makeOnlinePayment(OnlinePaymentRequest request) {
        try {
            var debitTransactionResponse = transactionService.debitMoneyFromBankAccount(request.getTransactionInfo());
            sendAckToPaymentGateway(debitTransactionResponse, request.getCallbackURL());

            return OnlinePaymentResponse.builder()
                    .type(TransactionType.DEBIT)
                    .success(true)
                    .build();
        } catch (TransactionException e) {
            var failedTransaction = TransactionResponse.builder()
                    .type(TransactionType.DEBIT)
                    .success(false)
                    .build();

            sendAckToPaymentGateway(failedTransaction, request.getCallbackURL());
            throw new OnlinePaymentException("Unable to make online payment",
                    FailedOnlinePaymentReason.valueOf(e.getReason().getValue()));
        }
    }

    private void sendAckToPaymentGateway(TransactionResponse transactionResponse, String callbackUrl) {
        var ack = PaymentGatewayAck.builder()
                .type(transactionResponse.getType().getValue())
                .success(transactionResponse.getSuccess())
                .build();

        ResponseEntity<Void> gatewayResponse = restClient.post()
                .uri(callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ack)
                .retrieve()
                .toBodilessEntity();

        if (gatewayResponse.getStatusCode().is2xxSuccessful()) {
            // log ok
        } else {
            // log fail
            throw new OnlinePaymentException("Unable to find a bank account balance by UUID.",
                    FailedOnlinePaymentReason.NO_RESPONSE_FROM_PAYMENT_GATEWAY_SERVER);
        }
    }
}
