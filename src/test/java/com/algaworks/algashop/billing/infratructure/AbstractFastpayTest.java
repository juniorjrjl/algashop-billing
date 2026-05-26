package com.algaworks.algashop.billing.infratructure;

import com.algaworks.algashop.billing.domain.model.creditcard.LimitedCreditCard;
import com.algaworks.algashop.billing.infratructure.creditcard.fastpay.CreditCardProviderServiceFastpayImpl;
import com.algaworks.algashop.billing.infratructure.creditcard.fastpay.FastpayCreditCardTokenizationAPIClient;
import com.algaworks.algashop.billing.infratructure.creditcard.fastpay.FastpayCreditCardTokenizationAPIClientConfig;
import com.algaworks.algashop.billing.infratructure.creditcard.fastpay.FastpayTokenizationRequest;
import com.algaworks.algashop.billing.utility.CustomFaker;
import com.algaworks.algashop.billing.utility.extension.WireMockExtension;
import com.algaworks.algashop.billing.utility.tag.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Year;
import java.util.UUID;

@IntegrationTest
@SpringBootTest
@Import({FastpayCreditCardTokenizationAPIClientConfig.class})
@ExtendWith(WireMockExtension.class)
public abstract class AbstractFastpayTest {

    protected static final CustomFaker customFaker = CustomFaker.getInstance();

    protected LimitedCreditCard limitedCreditCard;
    protected String alwaysPaidCardNumber = "4622943127011022";

    protected final FastpayCreditCardTokenizationAPIClient tokenizationAPIClient;
    protected final CreditCardProviderServiceFastpayImpl externalService;

    public AbstractFastpayTest(final FastpayCreditCardTokenizationAPIClient tokenizationAPIClient,
                               final CreditCardProviderServiceFastpayImpl externalService) {
        this.tokenizationAPIClient = tokenizationAPIClient;
        this.externalService = externalService;
    }

    @BeforeEach
    public void setup() {
        CustomFaker.getInstance().reseed();
        final var request = FastpayTokenizationRequest.builder()
                .number(alwaysPaidCardNumber)
                .cvv(customFaker.number().digits(3))
                .expYear(Year.now().plusYears(5).getValue())
                .expMonth(customFaker.timeAndDate().birthday().getMonthValue())
                .holderDocument(customFaker.cpf().valid(false))
                .holderName(customFaker.name().fullName())
                .build();
        final var tokenizedCreditCardResponse = tokenizationAPIClient.tokenize(request);
        limitedCreditCard = externalService.register(UUID.randomUUID(), tokenizedCreditCardResponse.getTokenizedCard());
    }

}
