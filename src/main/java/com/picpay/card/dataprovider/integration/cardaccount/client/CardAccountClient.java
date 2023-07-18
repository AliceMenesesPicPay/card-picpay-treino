package com.picpay.card.dataprovider.integration.cardaccount.client;

import com.picpay.card.dataprovider.integration.cardaccount.config.CardAccountConfig;
import com.picpay.card.dataprovider.integration.cardaccount.payload.response.CreditInfoResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
@AllArgsConstructor
public class CardAccountClient {

    private WebClient webClient;
    private CardAccountConfig cardAccountConfig;

    public CreditInfoResponse getCreditInfo(String consumerId) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(cardAccountConfig.getSchema())
                        .host(cardAccountConfig.getHost())
                        .path("/account/credit/info")
                        .queryParam("consumer_id", consumerId)
                        .build())
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CreditInfoResponse.class)
                .block();
    }

}
