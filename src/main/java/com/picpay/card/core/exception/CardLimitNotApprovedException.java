package com.picpay.card.core.exception;

public class CardLimitNotApprovedException extends BusinessException {

    public CardLimitNotApprovedException(String message) {
        super(String.format("Card cannot be created because consumer of id %s has no approved limit", message));
    }

}
