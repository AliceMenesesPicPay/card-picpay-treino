package com.picpay.card.entrypoint.api.controller;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.domain.usecase.CardRegistration;
import com.picpay.card.entrypoint.api.controller.contract.CardContract;
import com.picpay.card.entrypoint.api.controller.payload.request.CardDataRequest;
import com.picpay.card.entrypoint.api.controller.payload.response.CardResponse;
import com.picpay.card.entrypoint.api.mapper.CardDataMapper;
import com.picpay.card.entrypoint.api.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/v1/card")
@RestController
@RequiredArgsConstructor
public class CardController implements CardContract {

    private final CardRegistration cardRegistration;
    private final CardMapper cardMapper;
    private final CardDataMapper cardDataMapper;

    @Override
    @GetMapping
    public CardResponse search(final String consumerId) {
        Card card = cardRegistration.search(consumerId);
        return cardMapper.toResponse(card);
    }

    @Override
    @GetMapping("/consumer")
    public Page<CardResponse> list(Pageable pageable) {
        Page<Card> pageCards = cardRegistration.list(pageable);

        List<CardResponse> cards = cardMapper
                .toCollectionResponse(pageCards.getContent());

        return new PageImpl<>(cards, pageable, pageCards.getTotalElements());
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponse create(final String consumerId, final CardDataRequest cardDataRequest) {
        CardData cardData = cardDataMapper.toDomain(cardDataRequest);
        Card card = cardRegistration.create(consumerId, cardData);
        return cardMapper.toResponse(card);
    }

    @Override
    @PutMapping("/{id}")
    public CardResponse update(final String id, final String consumerId, final CardDataRequest cardDataRequest) {
        CardData cardData = cardDataMapper.toDomain(cardDataRequest);
        Card card = cardRegistration.update(id, consumerId, cardData);
        return cardMapper.toResponse(card);
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final String id, final String consumerId) {
        cardRegistration.delete(id, consumerId);
    }


}
