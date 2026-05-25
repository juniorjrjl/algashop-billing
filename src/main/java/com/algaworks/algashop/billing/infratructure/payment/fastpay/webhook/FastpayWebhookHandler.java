package com.algaworks.algashop.billing.infratructure.payment.fastpay.webhook;

import com.algaworks.algashop.billing.application.invoice.managment.InvoiceManagementApplicationService;
import com.algaworks.algashop.billing.infratructure.payment.fastpay.FastpayEnumConverter;
import com.algaworks.algashop.billing.infratructure.payment.fastpay.FastpayPaymentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class FastpayWebhookHandler {

    private final InvoiceManagementApplicationService applicationService;

    public void process(final FastpayPaymentWebhookEvent event) {
        log.info("Processing Fastpay Payment Webhook Event: {}", event);
        applicationService.updatePaymentStatus(
                UUID.fromString(event.getReferenceCode()),
                FastpayEnumConverter.convert(FastpayPaymentStatus.valueOf(event.getStatus()))
        );
    }
}
