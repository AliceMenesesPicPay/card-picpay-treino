package com.picpay.card.entrypoint.api.controller.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CardResponse {

    private String consumerId;
    private List<CardDataResponse> cards;

}
