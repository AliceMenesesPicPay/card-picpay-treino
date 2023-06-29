package com.picpay.card.core.gateway;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CardGateway {

    Optional<Card> search(String consumerId);

    Optional<Card> findByConsumerIdAndCardsType(String consumerId, CardType cardType);

    Card save(Card card);

    Page<Card> list(Pageable pageable);

    void delete(Card card);

}
