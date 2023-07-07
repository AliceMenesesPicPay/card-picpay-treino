package com.picpay.card.core.domain.usecase;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;
import com.picpay.card.core.exception.CardNotFoundException;
import com.picpay.card.core.exception.ThereIsPhysicalCardException;
import com.picpay.card.core.gateway.CardGateway;
import com.picpay.card.helper.CardDataGenerator;
import com.picpay.card.helper.CardGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Card Registration Test")
@ExtendWith(MockitoExtension.class)
class CardRegistrationTest {

    @Mock
    private CardGateway cardGateway;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CardRegistration cardRegistration;

    @Captor
    private ArgumentCaptor<Card> cardCaptor;

    @Test
    @DisplayName("When searching for the consumer's card, then return success")
    void whenSearchCardThenReturnSuccessfully() {
        String consumerId = "1";
        Optional<Card> expectedCard = CardGenerator.generatePhysicalCard(consumerId);

        when(cardGateway.search(consumerId)).thenReturn(expectedCard);

        Card realCard = cardRegistration.search(consumerId);

        assertThat(realCard).isEqualTo(expectedCard.get());
    }

    @Test
    @DisplayName("When searching for the consumer's card, then return exception there is no card registration for the consumer")
    void whenSearchCardThenReturnCardNotFoundException() {
        when(cardGateway.search(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardRegistration.search("1"))
                .isInstanceOf(CardNotFoundException.class)
                .hasMessage("Não existe um cadastro de cartão para o consumidor 1");
    }

    @Test
    @DisplayName("When to list consumer cards return cards page")
    void whenListCardThenReturnPageCard() {
        Pageable pageable = PageRequest.of(0,2);
        when(cardGateway.list(pageable)).thenReturn(CardGenerator.generatePageCard(pageable));

        Page<Card> pageCard = cardRegistration.list(pageable);
        assertThat(pageCard.getContent()).hasSize(2);
        assertThat(pageCard.getTotalPages()).isEqualTo(1);
        assertThat(pageCard.getTotalElements()).isEqualTo(2);
        assertThat(pageCard.getNumber()).isZero();
    }

    @Test
    @DisplayName("When creating a card data, allow only 1 physical card data per costumer, check if the customer already exists, encrypt the card data number and save it")
    void whenCreateCardDataThenReturnSuccessfully() {
        CardData argumentCardData = CardDataGenerator.generateCardData(null, "123123", "99023576012", "5345902181142126",
                "880", CardVariant.PLATINUM, "3815", CardType.FISICO, "Ana");

        String consumerId = "1";

        Optional<Card> searchCardOptional = CardGenerator.generateOnlyVirtualCard(consumerId);
        when(cardGateway.search(consumerId)).thenReturn(searchCardOptional);

        Optional<Card> saveCardOptional = CardGenerator.generateOnlyVirtualCard(consumerId);
        Card saveCard = saveCardOptional.get();

        CardData cardData = CardDataGenerator.generateCardData("7ca920f4-1aa6-11ee-be56-0242ac120002", "123123", "99023576012", "5345902181142126",
                "880", CardVariant.PLATINUM, "3815", CardType.FISICO, "Ana");
        saveCard.addCardData(cardData);
        when(cardGateway.save(cardCaptor.capture())).thenReturn(saveCard);

        Card expectedCard = CardGenerator.generateOnlyVirtualCard(consumerId).get();
        expectedCard.addCardData(cardData);

        Card realCard = cardRegistration.create(consumerId, argumentCardData);

        Card realCardCaptor = cardCaptor.getValue();
        assertThat(realCardCaptor.getCards().get(2).getNumCard()).isNotEqualTo(expectedCard.getCards().get(2).getNumCard());
        assertThat(realCard).usingRecursiveComparison().isEqualTo(expectedCard);
    }

    @Test
    @DisplayName("When creating the card validate for the consumer to have only 1 physical card")
    void whenTryingToCreateMoreThan1PhysicalCardDataThenThrowException() {
        CardData argumentCardData = CardDataGenerator.generateCardData(null, "123123", "99023576012", "5345902181142126",
                "880", CardVariant.PLATINUM, "3815", CardType.FISICO, "Ana");

        String consumerId = "1";

        Optional<Card> cardOptional = CardGenerator.generatePhysicalCard(consumerId);
        when(cardGateway.search(consumerId)).thenReturn(cardOptional);

        assertThatThrownBy(() -> cardRegistration.create(consumerId, argumentCardData))
                .isInstanceOf(ThereIsPhysicalCardException.class)
                .hasMessage("Já existe um cartão fisico para esse consumidor 1");
    }


    @Test
    @DisplayName("When creating a card data, when creating the card validate for the consumer to have only 1 physical card")
    void whenDeleteCardDataThenDeleteSuccessfully() {
        String consumerId = "1";

        Optional<Card> cardOptional = CardGenerator.generateOnlyVirtualCard(consumerId);
        when(cardGateway.search(consumerId)).thenReturn(cardOptional);


        cardRegistration.delete("685947c0-889e-43d5-b505-1abdf44d7d48", consumerId);
        verify(cardGateway).save(cardCaptor.capture());
        Card realCardCaptor = cardCaptor.getValue();

        assertThat(realCardCaptor.getCards()).hasSize(1);
    }

    @Test
    @DisplayName("When to delete all card data then delete card")
    void whenToDeleteAllCardDataThenDeleteCard() {
        String consumerId = "1";

        Optional<Card> cardOptional = CardGenerator.generateWithOneCardData(consumerId);
        when(cardGateway.search(consumerId)).thenReturn(cardOptional);


        cardRegistration.delete("685947c0-889e-43d5-b505-1abdf44d7d48", consumerId);
        verify(cardGateway).delete(cardCaptor.capture());

        Card realCardCaptor = cardCaptor.getValue();
        assertThat(realCardCaptor.getCards()).isEmpty();
    }

    @Test
    @DisplayName("When update card data return successfully")
    void whenUpdateCardDataThenReturnSuccessfully() {
        String id = "685947c0-889e-43d5-b505-1abdf44d7d48";
        String consumerId = "1";
        CardData argumentCardData = CardDataGenerator.generateCardData(null, "123123", "99023576012", "5374859255631091",
                "831", CardVariant.INTERNACIONAL, "1091", CardType.FISICO, "Ana");

        Optional<Card> searchCardOptional = CardGenerator.generatePhysicalCard(consumerId);
        when(cardGateway.search(consumerId)).thenReturn(searchCardOptional);

        Card expectedData = CardGenerator.generatePhysicalCard(consumerId).get();
        expectedData.getCards().remove(0);
        CardData expectedCardData = CardDataGenerator.generateCardData(id, "123123", "99023576012", "5374859255631091",
                "831", CardVariant.INTERNACIONAL, "1091", CardType.FISICO, "Ana");

        when(cardGateway.save(any(Card.class))).thenReturn(expectedData);

        Card realCard = cardRegistration.update(id, consumerId, argumentCardData);

        verify(modelMapper).map(any(CardData.class), any(CardData.class));

        assertThat(realCard).usingRecursiveComparison().isEqualTo(expectedData);

    }

    @Test
    @DisplayName("When trying to update if there is more than 1 physical card data throw an exception")
    void whenTryingToUpdateIfThereIsMoreThan1PhysicalCardDataThenThrowException() {
        String id = "2c15ce18-2632-4491-8805-bd8a61a92bda";
        String consumerId = "1";
        CardData argumentCardData = CardDataGenerator.generateCardData(null, "123123", "99023576012", "5374859255631091",
                "831", CardVariant.INTERNACIONAL, "1091", CardType.FISICO, "Ana");

        Optional<Card> searchCardOptional = CardGenerator.generatePhysicalCard(consumerId);
        when(cardGateway.search(consumerId)).thenReturn(searchCardOptional);

        assertThatThrownBy(() -> cardRegistration.update(id, consumerId, argumentCardData))
                .isInstanceOf(ThereIsPhysicalCardException.class)
                .hasMessage("Já existe um cartão fisico para esse consumidor 1");
    }

}
