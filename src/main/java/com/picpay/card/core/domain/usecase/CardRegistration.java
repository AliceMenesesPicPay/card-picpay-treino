package com.picpay.card.core.domain.usecase;

import com.picpay.card.core.common.utils.Crypto;
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

import javax.crypto.Cipher;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CardRegistration {

    private final CardGateway cardGateway;
    private final ModelMapper modelMapper;

    private static final String ALGORITHM = "RSA";

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
            cardGateway.findByConsumerIdAndCardsType(consumerId, cardData.getType()).ifPresent(status -> {
                throw new ThereIsPhysicalCardException(consumerId);
            });
        }

        encryptCardNumber(cardData);

        return cardGateway.save(card);
    }

    private void encryptCardNumber(CardData cardData) {
        String numCard = Crypto.encrypt(cardData.getNumCard());
        cardData.setNumCard(numCard);
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

        if (card.getAmountCardData() == 0) {
            cardGateway.delete(card);
        } else {
            cardGateway.save(card);
        }

    }

}
