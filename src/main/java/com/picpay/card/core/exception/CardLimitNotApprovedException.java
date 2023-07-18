package com.picpay.card.core.exception;

public class CardLimitNotApprovedException extends BusinessException {

    public CardLimitNotApprovedException(String message) {
        super(String.format("O cartão não pode ser criado porque o consumidor de id %s não tem limite aprovado", message));
    }

}
