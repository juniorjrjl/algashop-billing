package com.algaworks.algashop.billing.infratructure.payment.fastpay;

import com.algaworks.algashop.billing.domain.model.creditcard.CreditCard;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardRepository;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentRequest;
import com.algaworks.algashop.billing.infratructure.AbstractFastpayTest;
import com.algaworks.algashop.billing.infratructure.creditcard.fastpay.CreditCardProviderServiceFastpayImpl;
import com.algaworks.algashop.billing.infratructure.creditcard.fastpay.FastpayCreditCardTokenizationAPIClient;
import com.algaworks.algashop.billing.infratructure.creditcard.fastpay.FastpayCreditCardTokenizationAPIClientConfig;
import com.algaworks.algashop.billing.utility.databuilder.domain.PayerDataBuilder;
import com.algaworks.algashop.billing.utility.tag.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.UUID;

import static com.algaworks.algashop.billing.domain.model.invoice.PaymentMethod.CREDIT_CARD;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@SpringBootTest
@Import({FastpayCreditCardTokenizationAPIClientConfig.class})
class PaymentGatewayServiceFastpayImplTest extends AbstractFastpayTest {

    private final PaymentGatewayServiceFastpayImpl paymentGatewayServiceFastpay;
    private final CreditCardRepository creditCardRepository;

    @Autowired
    PaymentGatewayServiceFastpayImplTest(final PaymentGatewayServiceFastpayImpl paymentGatewayServiceFastpay,
                                         final FastpayCreditCardTokenizationAPIClient tokenizationAPIClient,
                                         final CreditCardRepository creditCardRepository,
                                         final CreditCardProviderServiceFastpayImpl externalService) {
        super(tokenizationAPIClient, externalService);
        this.creditCardRepository = creditCardRepository;
        this.paymentGatewayServiceFastpay = paymentGatewayServiceFastpay;
    }

    @Test
    void shouldProcessPaymentWithCreditCard(){
        final var creditCard = CreditCard.brandNew(
                UUID.randomUUID(),
                limitedCreditCard.getLastNumbers(),
                limitedCreditCard.getBrand(),
                limitedCreditCard.getExpMonth(),
                limitedCreditCard.getExpYear(),
                limitedCreditCard.getGatewayCode()
        );
        creditCardRepository.save(creditCard);
        final var request = PaymentRequest.builder()
                .paymentMethod(CREDIT_CARD)
                .amount(new BigDecimal("1000.00"))
                .invoiceId(UUID.randomUUID())
                .creditCardId(creditCard.getId())
                .payer(PayerDataBuilder.builder().build())
                .build();
        final var response = paymentGatewayServiceFastpay.capture(request);
        assertThat(response.getInvoiceId()).isEqualTo(request.getInvoiceId());
    }

}