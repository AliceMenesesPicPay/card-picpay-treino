package com.picpay.card.entrypoint.api.controller.contract;

import com.picpay.card.core.common.utils.HeaderName;
import com.picpay.card.core.domain.card.Card;
import com.picpay.card.entrypoint.api.controller.payload.request.CardDataRequest;
import com.picpay.card.entrypoint.api.controller.payload.response.CardResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

public interface CardContract {

    @ApiOperation("Endpoint responsável por retornar os cartões de crédito do consumidor")
    CardResponse search(
            @RequestParam(HeaderName.HEADER_CONSUMER_ID) String consumerId);

    @ApiOperation("Endpoint responsável por listar cartões de crédito de vários consumidores")
    Page<CardResponse> list(
            Pageable pageable);

    @ApiOperation("Endpoint responsável por criar cartão de crédito do consumidor")
    CardResponse create(
            @RequestHeader(HeaderName.HEADER_CONSUMER_ID) String consumerId,
            @RequestBody @Valid CardDataRequest cardDataRequest);

    @ApiOperation("Endpoint responsável por atualizar os cartões de crédito do consumidor")
    CardResponse update(
            @PathVariable String id,
            @RequestHeader(HeaderName.HEADER_CONSUMER_ID) String consumerId,
            @RequestBody @Valid CardDataRequest cardDataRequest);

    @ApiOperation("Endpoint responsável por deletar os cartões de crédito do consumidor")
    void delete(
            @PathVariable String id,
            @RequestHeader(HeaderName.HEADER_CONSUMER_ID) String consumerId);

}
