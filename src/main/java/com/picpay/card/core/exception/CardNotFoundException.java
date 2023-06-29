package com.picpay.card.core.exception;

public class CardNotFoundException extends EntityNotFoundException {
    public CardNotFoundException(String consumerId) {
        super(String.format("Não existe um cadastro de cartão para o consumidor %s", consumerId));
    }

}
