package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface FastpayCreditCardTokenizationAPIClient {

    @PostExchange("/api/v1/public/tokenized-cards")
    FastpayTokenizedCreditCardResponse tokenize(@RequestBody final FastpayTokenizationRequest request);

}
