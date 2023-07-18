package com.picpay.card.dataprovider.integration.cardaccount.payload.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditInfoResponse {

    private Boolean credit;
    private BigDecimal availableValue;

}
