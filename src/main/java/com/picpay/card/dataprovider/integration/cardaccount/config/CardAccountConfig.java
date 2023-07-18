package com.picpay.card.dataprovider.integration.cardaccount.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//TODO como adicionar o arquivo .env?
@Component
@ConfigurationProperties("card-account-config")
@Getter
@Setter
public class CardAccountConfig {

    private String schema;
    private String host;

}
