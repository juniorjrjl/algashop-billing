package com.algaworks.algashop.billing.infratructure.creditcard.fake;

import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardProviderService;
import com.algaworks.algashop.billing.domain.model.creditcard.LimitedCreditCard;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.Optional;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "algashop.integrations.payment.provider", havingValue = "FAKE")
public class CreditCardProviderServiceFakeImpl implements CreditCardProviderService {

    @Override
    public LimitedCreditCard register(final UUID customerId, final String tokenizedCard) {
        return fakeCard();
    }

    @Override
    public Optional<LimitedCreditCard> findById(final String gatewayCode) {
        return Optional.of(fakeCard());
    }

    @Override
    public void delete(final String gatewayCode) {

    }

    private static LimitedCreditCard fakeCard() {
        return LimitedCreditCard.builder()
                .brand("Visa")
                .expMonth(1)
                .expYear(Year.now().getValue() + 5)
                .gatewayCode(UUID.randomUUID().toString())
                .lastNumbers("1234")
                .build();
    }

}
