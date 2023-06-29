package com.picpay.card.core.domain.usecase;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.exception.CardNotFoundException;
import com.picpay.card.core.exception.ThereIsPhysicalCardException;
import com.picpay.card.core.gateway.CardGateway;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CardRegistration {

    private final CardGateway cardGateway;
    private final ModelMapper modelMapper;

    public Card search(final String consumerId) {
        return cardGateway.search(consumerId).orElseThrow(() -> new CardNotFoundException(consumerId));
    }

    public Page<Card> list(Pageable pageable) {
        return cardGateway.list(pageable);
    }

    public Card create(final String consumerId, final CardData cardData) {
        Card card;

        try {
            card = search(consumerId);
            card.addCardData(cardData);
        } catch (CardNotFoundException e) {
            card = Card.builder()
                    .consumerId(consumerId)
                    .cards(Arrays.asList(cardData))
                    .build();
        }

        if (CardType.isFisico(cardData.getType())) {
            cardGateway.findByConsumerIdAndCardsType(consumerId, cardData.getType()).ifPresent((status) -> {
                throw new ThereIsPhysicalCardException(consumerId);
            });
        }

        return cardGateway.save(card);
    }

    public Card update(final String id, final String consumerId, final CardData cardData) {
        Card currentCard = search(consumerId);

        CardData currentCardData = currentCard.getCardDataById(id);
        modelMapper.map(cardData, currentCardData);
        return cardGateway.save(currentCard);
    }

    public void delete(final String id, final String consumerId) {
        Card card = search(consumerId);
        CardData cardData = card.getCardDataById(id);
        card.deleteCardData(cardData);

        cardGateway.save(card);

        if (card.getAmountCardData() == 0) {

        }

    }

}
