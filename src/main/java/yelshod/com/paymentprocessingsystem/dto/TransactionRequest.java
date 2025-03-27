package yelshod.com.paymentprocessingsystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    private Long senderAccountId; // Для внутренних переводов
    private Long receiverAccountId;
    private Long senderUserId; //Для внешних переводов
    private Long receiverUserId;
    private BigDecimal amount;
}

