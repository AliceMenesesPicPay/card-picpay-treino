package com.picpay.card.helper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
//TODO deixar somente no pacote de testes
public class CustomPage<CardResponse> {

    private List<CardResponse> content;
    private long size;
    private long totalElements;
    private long totalPages;
    private long number;

}
