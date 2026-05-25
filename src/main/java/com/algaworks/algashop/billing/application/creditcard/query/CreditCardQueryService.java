package com.algaworks.algashop.billing.application.creditcard.query;

import java.util.List;
import java.util.UUID;

public interface CreditCardQueryService {

    CreditCardOutput findOne(final UUID id, final UUID customerId);

    List<CreditCardOutput> findByCustomer(final UUID customerId);

}
