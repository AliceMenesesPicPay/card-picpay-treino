package com.picpay.card.core.exception;

public class ThereIsPhysicalCardException extends BusinessException {

    public ThereIsPhysicalCardException(String consumerId) {
        super(String.format("Já existe um cartão fisico para esse consumidor %s", consumerId));
    }
    
}
