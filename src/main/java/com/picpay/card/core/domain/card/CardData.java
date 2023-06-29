package com.picpay.card.core.domain.card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardData {

    private String id = UUID.randomUUID().toString();
    private String cpf;
    private CardType type;
    private String num_card;
    private String conta_cartao;
    private String sufixo;
    private String cvv;
    private String embossing_name;
    private CardVariant variant;

}
