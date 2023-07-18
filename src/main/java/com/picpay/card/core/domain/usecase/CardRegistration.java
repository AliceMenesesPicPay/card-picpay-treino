package com.picpay.card.core.domain.usecase;

import com.picpay.card.core.common.utils.Crypto;
import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.exception.CardLimitNotApprovedException;
import com.picpay.card.core.exception.CardNotFoundException;
import com.picpay.card.core.gateway.CardAccountGateway;
import com.picpay.card.core.gateway.CardGateway;
import com.picpay.card.dataprovider.integration.cardaccount.payload.response.CreditInfoResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CardRegistration {

    private final CardAccountGateway cardAccountGateway;
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
            card.moreThanOnePhysicalCard(cardData);
            card.addCardData(cardData);
        } catch (CardNotFoundException e) {

            card = Card.builder()
                    .consumerId(consumerId)
                    .cards(Collections.singletonList(cardData))
                    .build();
        }

        CreditInfoResponse creditInfoResponse = cardAccountGateway.getCreditInfo(consumerId);

        if (!creditInfoResponse.isCredit()) {
            throw new CardLimitNotApprovedException(consumerId);
        }

        cardData.setAvailableValue(creditInfoResponse.getAvailableValue());

        encryptCardNumber(cardData);

        return cardGateway.save(card);
    }

    public Card update(final String id, final String consumerId, final CardData cardData) {
        cardData.setId(id);

        Card currentCard = search(consumerId);

        CardData currentCardData = currentCard.getCardDataById(id);
        currentCard.moreThanOnePhysicalCard(cardData);
        encryptCardNumber(cardData);

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

    private void encryptCardNumber(CardData cardData) {
        String numCard = Crypto.encrypt(cardData.getNumCard());
        cardData.setNumCard(numCard);
    }

}
