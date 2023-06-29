package com.picpay.card.core.domain.card;

public enum CardType {

    FISICO,
    VIRTUAL;

    public static boolean isFisico(CardType cardType) {
        return CardType.FISICO == cardType;
    }

}
