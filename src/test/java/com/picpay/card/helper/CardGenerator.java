package com.picpay.card.helper;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CardGenerator {

    public static Optional<Card> generatePhysicalCard(String consumerId) {
        return Optional.ofNullable(Card.builder()
                .consumerId(consumerId)
                .cards(CardDataGenerator.generateCardDataListWithPhysicalCard())
                .build());
    }

    public static Optional<Card> generateOnlyVirtualCard(String consumerId) {
        return Optional.ofNullable(Card.builder()
                .consumerId(consumerId)
                .cards(CardDataGenerator.generateCardDataListOnlyVirtualCard())
                .build());
    }

    public static Optional<Card> generateWithOneCardData(String consumerId) {
        List<CardData> cardDataList = new ArrayList<>();
        cardDataList.add(CardDataGenerator.generateCardData("685947c0-889e-43d5-b505-1abdf44d7d48", "123123", "99023576012", "5348450217109187",
                "201", CardVariant.BLACK, "9187", CardType.VIRTUAL, "Ana", new BigDecimal("1000")));

        return Optional.ofNullable(Card.builder()
                .consumerId(consumerId)
                .cards(cardDataList)
                .build());
    }

    public static Page<Card> generatePageCard(Pageable pageable) {
        Card card = Card.builder()
                .consumerId("1")
                .cards(CardDataGenerator.generateCardDataListWithPhysicalCard())
                .build();

        Card card2 = Card.builder()
                .consumerId("2")
                .cards(CardDataGenerator.generateCardDataListWithPhysicalCard())
                .build();

        List<Card> cards = List.of(card, card2);

        return new PageImpl<>(cards, pageable, cards.size());
    }

    public static Card generateCard(String consumerId, CardData cardData) {
        return Card.builder()
                .consumerId(consumerId)
                .cards(List.of(cardData))
                .build();
    }
}
