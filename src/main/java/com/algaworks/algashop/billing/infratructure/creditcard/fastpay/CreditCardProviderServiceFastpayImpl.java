package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardProviderService;
import com.algaworks.algashop.billing.domain.model.creditcard.LimitedCreditCard;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Optional;
import java.util.UUID;


@Service
@ConditionalOnProperty(name = "algashop.integrations.payment.provider", havingValue = "FASTPAY")
@RequiredArgsConstructor
public class CreditCardProviderServiceFastpayImpl implements CreditCardProviderService {

    private final FastpayCreditCardAPIClient fastpayCreditCardAPIClient;

    @Override
    public LimitedCreditCard register(final UUID customerId, final String tokenizedCard) {
        final var response = fastpayCreditCardAPIClient.create(FastpayCreditCardRequest.builder()
                        .tokenizedCard(tokenizedCard)
                        .customerCode(customerId.toString())
                .build());
        return LimitedCreditCard.builder()
                .brand(response.getBrand())
                .expMonth(response.getExpMonth())
                .expYear(response.getExpYear())
                .lastNumbers(response.getLastNumbers())
                .gatewayCode(response.getId())
                .build();
    }

    @Override
    public Optional<LimitedCreditCard> findById(final String gatewayCode) {
        final FastpayCreditCardResponse response;
        try{
            response = fastpayCreditCardAPIClient.findById(gatewayCode);
        } catch (final HttpClientErrorException.NotFound _) {
            return Optional.empty();
        }
        return Optional.of(response)
                .map(r -> LimitedCreditCard.builder()
                        .expYear(r.getExpYear())
                        .lastNumbers(r.getLastNumbers())
                        .gatewayCode(r.getId())
                        .build());
    }

    @Override
    public void delete(final String gatewayCode) {
        fastpayCreditCardAPIClient.delete(gatewayCode);
    }

}
