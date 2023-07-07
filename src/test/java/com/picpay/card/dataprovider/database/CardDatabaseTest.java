package com.picpay.card.dataprovider.database;

import com.picpay.card.core.domain.card.Card;
import com.picpay.card.dataprovider.database.repository.CardRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Card Registration Test")
@ExtendWith(MockitoExtension.class)
public class CardDatabaseTest {

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private CardDatabase cardDatabase;

    @Test
    @DisplayName("When search card by consumer id then return expected card")
    void whenSearchCardByConsumerIdThenReturnExpectedCard() {
        String consumerId = "any_consumer_id";
        Card card = new Card();

        when(cardRepository.findByConsumerId(consumerId)).thenReturn(Optional.of(card));

        Optional<Card> realCardOptional = cardDatabase.search(consumerId);

        verify(cardRepository).findByConsumerId(consumerId);

        assertThat(realCardOptional).usingRecursiveComparison().isEqualTo(Optional.of(card));
    }

    @Test
    @DisplayName("When save card then return saved card")
    void whenSaveCardThenReturnSavedCard() {
        Card card = new Card();

        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card realCard = cardDatabase.save(card);

        verify(cardRepository).save(card);

        assertThat(realCard).usingRecursiveComparison().isEqualTo(card);
    }

    @Test
    @DisplayName("When list card then return card page")
    void whenListCardThenReturnCardPage() {
        Card card = new Card();
        Page<Card> page = new PageImpl<>(List.of(card));

        when(cardRepository.findAll(any(Pageable.class))).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 5);

        Page<Card> realCardPage = cardDatabase.list(pageable);

        verify(cardRepository).findAll(pageable);

        assertThat(realCardPage).isNotNull();
    }

    @Test
    @DisplayName("When delete card then delete card")
    void whenDeleteCard() {
        Card card = new Card();

        cardDatabase.delete(card);

        verify(cardRepository).delete(card);
    }

}
