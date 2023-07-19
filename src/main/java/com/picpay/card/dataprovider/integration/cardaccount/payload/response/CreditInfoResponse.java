package com.picpay.card.dataprovider.integration.cardaccount.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreditInfoResponse {

    private boolean credit;
    private BigDecimal availableValue;

}
