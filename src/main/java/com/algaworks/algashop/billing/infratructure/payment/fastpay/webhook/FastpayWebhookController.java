package com.algaworks.algashop.billing.infratructure.payment.fastpay.webhook;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/webhook/fastpay")
@RequiredArgsConstructor
public class FastpayWebhookController {

    private final FastpayWebhookHandler handler;

    @PostMapping
    public void receive(@Valid @RequestBody final FastpayPaymentWebhookEvent event){
        handler.process(event);
    }

}
