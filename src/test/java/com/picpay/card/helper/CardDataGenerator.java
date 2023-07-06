package com.picpay.card.helper;

import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CardDataGenerator {

    public static List<CardData> generateCardDataListWithPhysicalCard() {
        List<CardData> cardDataList = new ArrayList<>();

        cardDataList.add(generateCardData("685947c0-889e-43d5-b505-1abdf44d7d48", "123123", "99023576012", "5348450217109187",
                "201", CardVariant.BLACK, "9187", CardType.FISICO, "Ana"));

        cardDataList.add(generateCardData("2c15ce18-2632-4491-8805-bd8a61a92bda", "123123", "99023576012", "5501488172193815",
                "159", CardVariant.INTERNACIONAL, "3815", CardType.VIRTUAL, "Ana"));

        return cardDataList;
    }

    public static List<CardData> generateCardDataListOnlyVirtualCard() {

        List<CardData> cardDataList = new ArrayList<>();

        cardDataList.add(generateCardData("685947c0-889e-43d5-b505-1abdf44d7d48", "123123", "99023576012", "5348450217109187",
                "201", CardVariant.BLACK, "9187", CardType.VIRTUAL, "Ana"));

        cardDataList.add(generateCardData("2c15ce18-2632-4491-8805-bd8a61a92bda", "123123", "99023576012", "5501488172193815",
                "159", CardVariant.INTERNACIONAL, "3815", CardType.VIRTUAL, "Ana"));

        return cardDataList;
    }

    public static CardData generateCardData(String id, String cardAccount, String cpf, String numCard,
                                     String cvv, CardVariant variant, String suffix, CardType type, String embossingName) {
        return CardData.builder()
                .id(id)
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
