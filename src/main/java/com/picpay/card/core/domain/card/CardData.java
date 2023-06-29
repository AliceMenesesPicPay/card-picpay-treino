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
    private String numCard;
    private String cardAccount;
    private String suffix;
    private String cvv;
    private String embossingName;
    private CardVariant variant;

}
