package com.picpay.card.helper;

import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;
import com.picpay.card.entrypoint.api.controller.payload.request.CardDataRequest;

import java.util.ArrayList;
import java.util.List;

public class CardDataRequestGenerator {

    public static CardDataRequest generateCardDataRequest(String cardAccount, String cpf, String numCard,
                                     String cvv, CardVariant variant, String suffix, CardType type, String embossingName) {
        return CardDataRequest.builder()
                .cardAccount(cardAccount)
                .cpf(cpf)
                .numCard(numCard)
                .cvv(cvv)
                .variant(variant)
                .suffix(suffix)
                .type(type)
                .embossingName(embossingName)
                .build();
    }

}
