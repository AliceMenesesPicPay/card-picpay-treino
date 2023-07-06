package com.picpay.card.core.domain.card;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.picpay.card.core.exception.CardDataNotFoundException;
import com.picpay.card.core.exception.ThereIsPhysicalCardException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "card")
public class Card {

    @Id
    private String consumerId;
    private List<CardData> cards;

    public void addCardData(CardData cardData) {
        cards.add(cardData);
    }

    public CardData getCardDataById(String id) {
        return cards.stream()
                .filter(cardData -> cardData.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new CardDataNotFoundException(id));
    }

    public void moreThanOnePhysicalCard(CardData cardData) {
        if (CardType.isFisico(cardData.getType())) {
            cards.stream()
                    .filter(c -> CardType.isFisico(c.getType()) && !c.getId().equals(cardData.getId()))
                    .findAny()
                    .ifPresent(ca -> {
                        throw new ThereIsPhysicalCardException(consumerId);
                    });
        }
    }

    public int getAmountCardData() {
        return cards.size();
    }

    public void deleteCardData(CardData cardData) {
        cards.remove(cardData);
    }

}
