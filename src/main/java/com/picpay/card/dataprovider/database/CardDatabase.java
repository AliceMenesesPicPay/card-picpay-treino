package com.picpay.card.dataprovider.database;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.gateway.CardGateway;
import com.picpay.card.dataprovider.database.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardDatabase implements CardGateway {

    private final CardRepository cardRepository;

    @Override
    public Optional<Card> search(String consumerId) {
        return cardRepository.findByConsumerId(consumerId);
    }

    @Override
    public Optional<Card> findByConsumerIdAndCardsType(String consumerId, CardType cardType) {
        return cardRepository.findByConsumerIdAndCardsType(consumerId, cardType);
    }

    @Override
    public Card save(Card card) {
        return cardRepository.save(card);
    }

    @Override
    public Page<Card> list(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    @Override
    public void delete(Card card) {
        cardRepository.delete(card);
    }

}
