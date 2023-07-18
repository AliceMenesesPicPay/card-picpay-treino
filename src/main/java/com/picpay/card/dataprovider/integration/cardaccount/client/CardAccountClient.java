package com.picpay.card.dataprovider.integration.cardaccount.client;

import com.picpay.card.dataprovider.integration.cardaccount.payload.response.CreditInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
public class CardAccountClient {

    private final static String cardAccountUrl = "http://card-account.ms.qa/account";

    @Autowired
    private WebClient webClient;

    public CreditInfoResponse getCreditInfo(String consumerId) {
        return webClient
                .get()
                .uri(cardAccountUrl + "/credit/info?consumer_id=" + consumerId)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CreditInfoResponse.class)
                .block();
    }


}
