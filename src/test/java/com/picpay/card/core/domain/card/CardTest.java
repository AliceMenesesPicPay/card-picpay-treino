package com.picpay.card.core.domain.card;

import com.picpay.card.core.exception.CardDataNotFoundException;
import com.picpay.card.core.exception.ThereIsPhysicalCardException;
import com.picpay.card.helper.CardDataGenerator;
import com.picpay.card.helper.CardGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Card Test")
public class CardTest {

    @Test
    @DisplayName("When get CardData by id then return expected CardData")
    void whenGetCardDataByIdThenReturnExpectedCardData() {

        String id = "685947c0-889e-43d5-b505-1abdf44d7d48";
        Card card = CardGenerator.generatePhysicalCard(id).get();

        CardData realCard = card.getCardDataById(id);

        assertThat(realCard).usingRecursiveComparison().isEqualTo(card.getCards().get(0));
    }

    @Test
    @DisplayName("When get CardData by id and this card data does not exist then return exception")
    void whenGetCardDataByIdAndThisCardDataDoesNotExistThenReturnExpectedCardData() {

        String consumerId = "1";
        Card card = CardGenerator.generatePhysicalCard(consumerId).get();

        String id = "1";

        assertThatThrownBy(() -> card.getCardDataById(id))
                .isInstanceOf(CardDataNotFoundException.class)
                .hasMessage(String.format("Não existe um cartão de id %s", id));
    }

    @Test
    @DisplayName("When the card data is physical and a physical card already exists then throw an exception")
    void whenCardDataIsPhysicalAndAPhysicalCardAlreadyExistsThenThrowAnException() {
        String id = "c90a3e59-5d7d-4e92-b73f-b77e806d89d9";
        CardData cardData = CardDataGenerator.generateCardData(id, "any card account", "any cpf", "any num card", "any cvv", CardVariant.BLACK, "any suffix", CardType.FISICO, "any embossing name", new BigDecimal("1000"));

        String consumerId = "1";
        Card card = CardGenerator.generatePhysicalCard(consumerId).get();

        assertThatThrownBy(() -> card.moreThanOnePhysicalCard(cardData))
                .isInstanceOf(ThereIsPhysicalCardException.class)
                .hasMessage(String.format("Já existe um cartão fisico para esse consumidor %s", consumerId));
    }

    @Test
    @DisplayName("When the card data is physical and it is the same card data in the list then do not throw an exception")
    void whenCardDataIsPhysicalAndItIsTheSameCardDataInTheListThenDoNotThrowAnException() {
        String consumerId = "1";
        Card card = CardGenerator.generatePhysicalCard(consumerId).get();

        String id = card.getCards().get(0).getId();
        CardData cardData = CardDataGenerator.generateCardData(id, "any card account", "any cpf", "any num card", "any cvv", CardVariant.BLACK, "any suffix", CardType.FISICO, "any embossing name", new BigDecimal("1000"));

        assertThatCode(() -> card.moreThanOnePhysicalCard(cardData)).doesNotThrowAnyException();
    }

}
