package com.picpay.card.entrypoint.api.controller.payload.request;

import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
public class CardDataRequest {

    @CPF
    @NotBlank
    private String cpf;

    @NotNull
    private CardType type;

    @CreditCardNumber
    @NotBlank
    private String numCard;

    @NotBlank
    private String cardAccount;

    @NotBlank
    private String suffix;

    @Size(min = 3, max = 3)
    @NotBlank
    private String cvv;

    @NotBlank
    private String embossingName;

    @NotNull
    private CardVariant variant;

}
