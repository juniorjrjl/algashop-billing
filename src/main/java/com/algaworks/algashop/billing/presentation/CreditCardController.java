package com.algaworks.algashop.billing.presentation;

import com.algaworks.algashop.billing.application.creditcard.management.CreditCardManagementService;
import com.algaworks.algashop.billing.application.creditcard.management.TokenizedCreditCardInput;
import com.algaworks.algashop.billing.application.creditcard.query.CreditCardOutput;
import com.algaworks.algashop.billing.application.creditcard.query.CreditCardQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/{version}/customers/{customerId}/credit-cards")
@RequiredArgsConstructor
public class CreditCardController {

    private final CreditCardManagementService managementService;
    private final CreditCardQueryService queryService;

    @PostMapping
    @ResponseStatus(CREATED)
    public CreditCardOutput register(@PathVariable final UUID customerId,
                                     @RequestBody @Valid final TokenizedCreditCardInput input){
        input.setCustomerId(customerId);
        final var id = managementService.register(input);
        return queryService.findOne(id, customerId);
    }

    @GetMapping
    public List<CreditCardOutput> findAllByCustomer(@PathVariable final UUID customerId) {
        return queryService.findByCustomer(customerId);
    }

    @GetMapping("/{creditCardId}")
    public CreditCardOutput findOne(@PathVariable final UUID customerId, @PathVariable final UUID creditCardId) {
        return queryService.findOne(creditCardId, customerId);
    }

    @DeleteMapping("/{creditCardId}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable final UUID customerId, @PathVariable final UUID creditCardId) {
        managementService.delete(creditCardId, customerId);
    }

}
