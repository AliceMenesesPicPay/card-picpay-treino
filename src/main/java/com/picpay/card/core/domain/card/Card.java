package com.picpay.card.core.domain.card;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.picpay.card.core.exception.CardDataNotFoundException;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public int getAmountCardData() {
        return cards.size();
    }

    public void deleteCardData(CardData cardData) {
        cards.remove(cardData);
    }

}
