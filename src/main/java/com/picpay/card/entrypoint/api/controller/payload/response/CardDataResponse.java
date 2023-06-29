package com.picpay.card.entrypoint.api.controller.payload.response;

import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CardDataResponse {

    @NotBlank
    private String id;
    private String cpf;
    private CardType type;
    private String num_card;
    private String conta_cartao;
    private String sufixo;
    private String cvv;
    private String embossing_name;
    private CardVariant variant;

}
