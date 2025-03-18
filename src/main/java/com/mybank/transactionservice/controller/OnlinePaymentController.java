package com.mybank.transactionservice.controller;

import com.mybank.transactionservice.api.OnlinePaymentApi;
import com.mybank.transactionservice.api.model.OnlinePaymentRequest;
import com.mybank.transactionservice.api.model.OnlinePaymentResponse;
import com.mybank.transactionservice.service.OnlinePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OnlinePaymentController implements OnlinePaymentApi {

    private final OnlinePaymentService onlinePaymentService;

    @Override
    public ResponseEntity<OnlinePaymentResponse> makeOnlinePayment(OnlinePaymentRequest request) {
        return ResponseEntity.ok(onlinePaymentService.makeOnlinePayment(request));
    }
}
