package com.algaworks.algashop.billing.domain.model.creditcard;

import java.util.Optional;
import java.util.UUID;

public interface CreditCardProviderService {

    LimitedCreditCard register(final UUID customerId, final String tokenizedCard);

    Optional<LimitedCreditCard> findById(final String gatewayCode);

    void delete(final String gatewayCode);

}
