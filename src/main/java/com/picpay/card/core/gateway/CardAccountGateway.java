package com.picpay.card.core.gateway;

import com.picpay.card.dataprovider.integration.cardaccount.payload.response.CreditInfoResponse;

public interface CardAccountGateway {

    CreditInfoResponse getCreditInfo(String consumerId);

}
