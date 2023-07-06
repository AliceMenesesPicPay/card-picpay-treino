package com.picpay.card.dataprovider.database.repository;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends PagingAndSortingRepository<Card, String> {

    Optional<Card> findByConsumerId(String consumerId);

//    Optional<Card> findByConsumerIdAndCardsType(String consumerId, CardType cardType);

}
