package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import com.algaworks.algashop.billing.infratructure.AbstractFastpayTest;
import com.algaworks.algashop.billing.utility.tag.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@SpringBootTest
@Import({FastpayCreditCardTokenizationAPIClientConfig.class})
class CreditCardProviderServiceFastpayImplTest extends AbstractFastpayTest {

    @Autowired
    CreditCardProviderServiceFastpayImplTest(final CreditCardProviderServiceFastpayImpl externalService,
                                             final FastpayCreditCardTokenizationAPIClient tokenizationAPIClient) {
        super(tokenizationAPIClient, externalService);
    }

    @Test
    void shouldRegisterCreditCard() {
        assertThat(limitedCreditCard.getGatewayCode()).isNotBlank();
    }

    @Test
    void shouldFindRegisteredCreditCard() {
        final var actual = externalService.findById(limitedCreditCard.getGatewayCode())
                .orElseThrow();
        assertThat(actual.getGatewayCode()).isEqualTo(limitedCreditCard.getGatewayCode());
    }

    @Test
    void shouldDeleteRegisteredCreditCard() {
        externalService.delete(limitedCreditCard.getGatewayCode());
        final var actual = externalService.findById(limitedCreditCard.getGatewayCode());
        assertThat(actual).isEmpty();
    }

}