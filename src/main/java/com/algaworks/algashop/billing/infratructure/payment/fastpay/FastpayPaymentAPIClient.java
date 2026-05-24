package com.algaworks.algashop.billing.infratructure.payment.fastpay;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@HttpExchange(value = "/api/v1/payments", accept = APPLICATION_JSON_VALUE)
public interface FastpayPaymentAPIClient {

    @PostExchange(contentType = APPLICATION_JSON_VALUE)
    FastpayPaymentResponse capture(@RequestBody final FastpayPaymentRequest request);

    @GetExchange("/{paymentId}")
    FastpayPaymentResponse findById(@RequestParam final String paymentId);

    @PutExchange("/{paymentId}/refund")
    void refund(@RequestParam final String paymentId);

    @PutExchange("/{paymentId}/cancel")
    void cancel(@RequestParam final String paymentId);

}
