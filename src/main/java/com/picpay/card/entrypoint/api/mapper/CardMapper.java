package com.picpay.card.entrypoint.api.mapper;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.entrypoint.api.controller.payload.request.CardDataRequest;
import com.picpay.card.entrypoint.api.controller.payload.response.CardDataResponse;
import com.picpay.card.entrypoint.api.controller.payload.response.CardResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CardMapper {

    private final ModelMapper modelMapper;

    public CardResponse toResponse(final Card card) {
        return modelMapper.map(card, CardResponse.class);
    }

    public List<CardResponse> toCollectionResponse(final List<Card> cards) {
        return cards.stream().map(card -> modelMapper.map(card, CardResponse.class)).collect(Collectors.toList());
    }

    public Card toDomain(final String consumerId, final CardDataRequest cardDataRequest) {
        return modelMapper.map(cardDataRequest, Card.class);
    }

}
