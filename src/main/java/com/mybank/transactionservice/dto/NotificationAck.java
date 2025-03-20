package com.mybank.transactionservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationAck {

    private String type;
    private Boolean success;
}
