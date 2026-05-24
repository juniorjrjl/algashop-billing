package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import org.jspecify.annotations.Nullable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@HttpExchange(value = "/api/v1/credit-cards", accept = APPLICATION_JSON_VALUE)
public interface FastpayCreditCardAPIClient {

    @PostExchange(contentType = APPLICATION_JSON_VALUE)
    FastpayCreditCardResponse create(@RequestBody final FastpayCreditCardRequest request);

    @GetExchange("/{creditCardId}")
    FastpayCreditCardResponse findById(@PathVariable final String creditCardId);

    @DeleteExchange("/{creditCardId}")
    void delete(@PathVariable final String creditCardId);

}
