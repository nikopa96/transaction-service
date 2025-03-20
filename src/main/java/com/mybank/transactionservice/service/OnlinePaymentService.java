package com.mybank.transactionservice.service;

import com.mybank.transactionservice.api.model.*;
import com.mybank.transactionservice.dto.NotificationAck;
import com.mybank.transactionservice.exception.OnlinePaymentException;
import com.mybank.transactionservice.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class OnlinePaymentService {

    private final TransactionService transactionService;
    private final RestClient restClient;

    /**
     * This method debits money from the account and sends a notification to an external service,
     * such as an external payment gateway of an online store.
     * @param request debit money request with an external service callback
     * @return successful response body
     */
    public OnlinePaymentResponse makeOnlinePayment(OnlinePaymentRequest request) {
        try {
            var debitTransactionResponse = transactionService.debitMoneyFromBankAccount(request.getTransactionInfo());
            sendNotificationToExternalService(debitTransactionResponse, request.getCallbackURL());

            return OnlinePaymentResponse.builder()
                    .type(TransactionType.DEBIT)
                    .success(true)
                    .build();
        } catch (TransactionException e) {
            var failedTransaction = TransactionResponse.builder()
                    .type(TransactionType.DEBIT)
                    .success(false)
                    .build();

            sendNotificationToExternalService(failedTransaction, request.getCallbackURL());

            throw new OnlinePaymentException("Unable to make online payment",
                    FailedOnlinePaymentReason.valueOf(e.getReason().getValue()));
        }
    }

    private void sendNotificationToExternalService(TransactionResponse transactionResponse, String callbackUrl) {
        var ack = NotificationAck.builder()
                .type(transactionResponse.getType().getValue())
                .success(transactionResponse.getSuccess())
                .build();

        try {
            restClient.post()
                    .uri(callbackUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ack)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpStatusCodeException e) {
            log.error("Unable to send an ack to external payment service. Status code: {}", e.getStatusCode().value());
        }
    }
}
