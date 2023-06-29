package com.picpay.card.entrypoint.api.mapper;

import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.entrypoint.api.controller.payload.request.CardDataRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardDataMapper {

    private final ModelMapper modelMapper;

    public CardData toDomain(final CardDataRequest cardDataRequest) {
        return modelMapper.map(cardDataRequest, CardData.class);
    }

}
