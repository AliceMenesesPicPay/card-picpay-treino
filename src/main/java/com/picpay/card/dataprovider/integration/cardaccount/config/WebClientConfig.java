package com.picpay.card.dataprovider.integration.cardaccount.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

//E se tivese vários client que usam webflux, em que pacote eu deixaria essa config?
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder.build();
    }

}
