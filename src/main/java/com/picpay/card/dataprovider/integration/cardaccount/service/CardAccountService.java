package com.picpay.card.dataprovider.integration.cardaccount.service;

import com.picpay.card.core.gateway.CardAccountGateway;
import com.picpay.card.dataprovider.integration.cardaccount.client.CardAccountClient;
import com.picpay.card.dataprovider.integration.cardaccount.payload.response.CreditInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardAccountService implements CardAccountGateway {

    private final CardAccountClient cardAccountClient;

    @Override
    public CreditInfoResponse getCreditInfo(String consumerId) {
        return cardAccountClient.getCreditInfo(consumerId);
    }

}
