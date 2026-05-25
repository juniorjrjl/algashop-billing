package com.algaworks.algashop.billing.presentation;

import com.algaworks.algashop.billing.application.invoice.managment.GenerateInvoiceInput;
import com.algaworks.algashop.billing.application.invoice.managment.InvoiceManagementApplicationService;
import com.algaworks.algashop.billing.application.invoice.query.InvoiceOutput;
import com.algaworks.algashop.billing.application.invoice.query.InvoiceQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/{version}/orders/{orderId}/invoices")
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final InvoiceQueryService queryService;
    private final InvoiceManagementApplicationService applicationService;

    @PostMapping
    @ResponseStatus(CREATED)
    public InvoiceOutput generate(@PathVariable final String orderId,
                                  @RequestBody @Valid final GenerateInvoiceInput input) {
        input.setOrderId(orderId);
        final var invoiceId = applicationService.generate(input);
        try{
            applicationService.processPayment(invoiceId);
        } catch(Exception ex){
            log.error("Error processing payment for invoice {}", orderId, ex);
        }
        return queryService.findByOrderId(orderId);
    }

    @GetMapping
    public InvoiceOutput findByOrder(@PathVariable final String orderId){
        return queryService.findByOrderId(orderId);
    }

}
