package com.picpay.card.entrypoint.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.picpay.card.core.domain.card.Card;
import com.picpay.card.core.domain.card.CardData;
import com.picpay.card.core.domain.card.CardType;
import com.picpay.card.core.domain.card.CardVariant;
import com.picpay.card.core.domain.usecase.CardRegistration;
import com.picpay.card.core.exception.ApiError;
import com.picpay.card.core.exception.CardNotFoundException;
import com.picpay.card.entrypoint.api.controller.payload.request.CardDataRequest;
import com.picpay.card.entrypoint.api.controller.payload.response.CardResponse;
import com.picpay.card.entrypoint.api.mapper.CardDataMapper;
import com.picpay.card.entrypoint.api.mapper.CardMapper;
import com.picpay.card.helper.CardDataRequestGenerator;
import com.picpay.card.helper.CardGenerator;
import com.picpay.card.helper.CustomPage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CardController.class)
@DisplayName("Card Controller Test")
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CardMapper cardMapper;

    @MockBean
    private CardDataMapper cardDataMapper;

    @MockBean
    private CardRegistration cardRegistration;

    @Test
    @DisplayName("When search card then return succeffuly")
    void whenSearchCardThenReturnSucceffuly() throws Exception {

        String consumerId = "1";
        Card card = CardGenerator.generatePhysicalCard(consumerId).get();
        CardResponse expectedCardResponse = objectMapper.convertValue(card, CardResponse.class);

        when(cardRegistration.search(consumerId)).thenReturn(card);
        when(cardMapper.toResponse(card)).thenReturn(expectedCardResponse);


        MvcResult result = mockMvc.perform(get("/api/v1/cards/consumer")
                .param("consumer_id", consumerId))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        CardResponse realCardResponse = objectMapper.readValue(contentAsString, CardResponse.class);

        Assertions.assertThat(realCardResponse).usingRecursiveComparison().isEqualTo(expectedCardResponse);
    }

    @Test
    @DisplayName("When search card that does not exist then return exception")
    void whenSearchCardThatDoesNotExistThenReturnException() throws Exception {

        String consumerIdThatDoesNotExist = "45";
        String expectedMessage = "Não existe um cadastro de cartão para o consumidor %s";

        CardNotFoundException cardNotFoundException = new CardNotFoundException(consumerIdThatDoesNotExist);
        when(cardRegistration.search(consumerIdThatDoesNotExist)).thenThrow(cardNotFoundException);

        mockMvc.perform(get("/api/v1/cards/consumer")
                .param("consumer_id", consumerIdThatDoesNotExist))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(String.format(expectedMessage, consumerIdThatDoesNotExist), result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("when list card then return page card")
    void whenListCardThenReturnPageCard() throws Exception {
        Pageable pageable = PageRequest.of(0,1);

        Page<Card> pageCards = CardGenerator.generatePageCard(pageable);
        List<CardResponse> expectedListCardResponse = objectMapper.convertValue(pageCards.getContent(), new TypeReference<List<CardResponse>>(){});

        when(cardRegistration.list(pageable)).thenReturn(pageCards);
        when(cardMapper.toCollectionResponse(pageCards.getContent())).thenReturn(objectMapper.convertValue(pageCards.getContent(), new TypeReference<List<CardResponse>>(){}));


        MvcResult result = mockMvc.perform(get("/api/v1/cards")
                .param("page", "0")
                .param("size", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = raddesult.getResponse().getContentAsString();

        CustomPage<CardResponse> realCardResponse = objectMapper.readValue(contentAsString, new TypeReference<CustomPage<CardResponse>>(){});

        assertThat(realCardResponse.getContent()).hasSize(2);
        assertThat(realCardResponse.getTotalPages()).isEqualTo(2);
        assertThat(realCardResponse.getTotalElements()).isEqualTo(2);
        assertThat(realCardResponse.getNumber()).isZero();
        assertThat(realCardResponse.getContent()).usingRecursiveComparison().isEqualTo(expectedListCardResponse);
    }

    @Test
    @DisplayName("when create card then return succeffuly")
    void whenCreateCardThenReturnSucceffuly() throws Exception {
        String consumerId = "23";
        CardDataRequest cardDataRequest = CardDataRequestGenerator.generateCardDataRequest("343434", "50107746077", "5406843539356354", "911", CardVariant.BLACK, "6354", CardType.VIRTUAL, "Gabi");

        CardData cardData = objectMapper.convertValue(cardDataRequest, CardData.class);
        when(cardDataMapper.toDomain(any(CardDataRequest.class))).thenReturn(cardData);
        Card card = CardGenerator.generateCard(consumerId, cardData);
        when(cardRegistration.create(consumerId, cardData)).thenReturn(card);
        CardResponse expectedCardResponse = objectMapper.convertValue(card, CardResponse.class);
        when(cardMapper.toResponse(card)).thenReturn(expectedCardResponse);

        MvcResult result = mockMvc.perform(post("/api/v1/cards")
                        .contentType("application/json")
                        .header("consumer_id", consumerId)
                        .content(objectMapper.writeValueAsString(cardDataRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        CardResponse realCardResponse = objectMapper.readValue(contentAsString, CardResponse.class);

        assertThat(realCardResponse).usingRecursiveComparison().isEqualTo(expectedCardResponse);
    }

    @Test
    @DisplayName("when create invalid card then return exception")
    void whenCreateCardInvalidThenReturnException() throws Exception {

        String consumerId = "23";
        CardDataRequest invalidCardDataRequest = CardDataRequestGenerator.generateCardDataRequest(null, "", "", "", null, "", null, "");

        MvcResult result = mockMvc.perform(post("/api/v1/cards")
                        .contentType("application/json")
                        .header("consumer_id", consumerId)
                        .content(objectMapper.writeValueAsString(invalidCardDataRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        ApiError realApiError = objectMapper.readValue(contentAsString, ApiError.class);

        assertThat(realApiError.getDescriptions()).hasSize(11);
        assertThat(realApiError.getDescription()).isNotEmpty();

//        assertThat(realApiError).usingRecursiveComparison().isEqualTo(expectedApiError); Spring acha que o locale é ingles, entao mensagens chegam aqui e ingles
    }

    @Test
    @DisplayName("when update invalid card then return exception")
    void whenUpdateInvalidCardThenReturnException() throws Exception {
        String consumerId = "23";
        String cardDataId = "685947c0-889e-43d5-b505-1abdf44d7d48";
        CardDataRequest invalidCardDataRequest = CardDataRequestGenerator.generateCardDataRequest(null, "", "", "", null, "", null, "");

        MvcResult result = mockMvc.perform(put("/api/v1/cards/{id}", cardDataId)
                        .contentType("application/json")
                        .header("consumer_id", consumerId)
                        .content(objectMapper.writeValueAsString(invalidCardDataRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        ApiError realApiError = objectMapper.readValue(contentAsString, ApiError.class);

        assertThat(realApiError.getDescriptions()).hasSize(11);
        assertThat(realApiError.getDescription()).isNotEmpty();
    }

    @Test
    @DisplayName("when update card then return succeffuly")
    void whenUpdateCardThenReturnSucceffuly() throws Exception {
        String consumerId = "23";
        String cardDataId = "685947c0-889e-43d5-b505-1abdf44d7d48";
        CardDataRequest cardDataRequest = CardDataRequestGenerator.generateCardDataRequest("343434", "50107746077", "5406843539356354", "911", CardVariant.BLACK, "6354", CardType.VIRTUAL, "Gabi");

        CardData cardData = objectMapper.convertValue(cardDataRequest, CardData.class);
        when(cardDataMapper.toDomain(any(CardDataRequest.class))).thenReturn(cardData);

        Card card = CardGenerator.generateCard(consumerId, cardData);
        when(cardRegistration.update(anyString(), anyString(), any(CardData.class))).thenReturn(card);

        CardResponse expectedCardResponse = objectMapper.convertValue(card, CardResponse.class);
        when(cardMapper.toResponse(card)).thenReturn(expectedCardResponse);

        MvcResult result = mockMvc.perform(put("/api/v1/cards/{id}", cardDataId)
                        .contentType("application/json")
                        .header("consumer_id", consumerId)
                        .content(objectMapper.writeValueAsString(cardDataRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();

        CardResponse realCardResponse = objectMapper.readValue(contentAsString, CardResponse.class);

        assertThat(realCardResponse).usingRecursiveComparison().isEqualTo(expectedCardResponse);
    }

    @Test
    @DisplayName("when delete card then return succeffuly")
    void whenDeleteCardThenReturnSucceffuly() throws Exception {
        String consumerId = "23";
        String cardDataId = "685947c0-889e-43d5-b505-1abdf44d7d48";

       mockMvc.perform(delete("/api/v1/cards/{id}", cardDataId)
                        .header("consumer_id", consumerId))
                .andExpect(status().isNoContent());

        verify(cardRegistration).delete(cardDataId, consumerId);

    }

}
