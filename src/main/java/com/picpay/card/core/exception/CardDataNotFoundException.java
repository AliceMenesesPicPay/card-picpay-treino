package com.picpay.card.core.exception;

public class CardDataNotFoundException extends EntityNotFoundException {
    public CardDataNotFoundException(String id) {
        super(String.format("Não existe um cartão de id %s", id));
    }

}
