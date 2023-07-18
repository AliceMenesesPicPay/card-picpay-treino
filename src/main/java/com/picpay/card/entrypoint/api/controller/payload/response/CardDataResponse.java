package com.picpay.card.entrypoint.api.controller.payload.response;

import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Getter
@Setter
public class CardDataResponse {

    @NotBlank
    private String id;
    private String cpf;
    private CardType type;
    private String numCard;
    private String cardAccount;
    private String suffix;
    private String cvv;
    private String embossingName;
    private CardVariant variant;
    private BigDecimal availableValue;

}
