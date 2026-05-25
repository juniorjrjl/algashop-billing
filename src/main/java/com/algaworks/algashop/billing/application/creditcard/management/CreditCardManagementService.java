package com.algaworks.algashop.billing.application.creditcard.management;

import com.algaworks.algashop.billing.domain.model.creditcard.CreditCard;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardNotFoundException;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardProviderService;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreditCardManagementService {

    private final CreditCardRepository repository;
    private final CreditCardProviderService providerService;

    @Transactional
    public UUID register(final TokenizedCreditCardInput input){
        final var limitedCreditCard = providerService.register(input.getCustomerId(), input.getTokenizedCard());
        final var creditCard = CreditCard.brandNew(
                input.getCustomerId(),
                limitedCreditCard.getLastNumbers(),
                limitedCreditCard.getBrand(),
                limitedCreditCard.getExpMonth(),
                limitedCreditCard.getExpYear(),
                limitedCreditCard.getGatewayCode()
        );
        repository.save(creditCard);
        return creditCard.getId();
    }

    @Transactional
    public void delete(final UUID id, final UUID customerId){
        final var creditCard = repository.findByIdAndCustomerId(id, customerId)
                .orElseThrow(CreditCardNotFoundException::new);
        repository.delete(creditCard);
        providerService.delete(creditCard.getGatewayCode());
    }

}
