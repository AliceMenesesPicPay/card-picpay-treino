package com.picpay.card.dataprovider.integration.cardaccount.payload.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Limit {

    private BigDecimal availableValue;

}
