package com.picpay.card.dataprovider.database.repository;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends MongoRepository<Card, String> {

    Optional<Card> findByConsumerId(String consumerId);

    Optional<Card> findByConsumerIdAndCardsType(String consumerId, CardType cardType);

}
