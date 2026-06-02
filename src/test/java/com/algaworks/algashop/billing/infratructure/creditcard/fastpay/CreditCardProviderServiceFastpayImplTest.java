package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import com.algaworks.algashop.billing.infratructure.AbstractFastpayTest;
import com.algaworks.algashop.billing.utility.extension.PGContainer;
import com.algaworks.algashop.billing.utility.extension.PostgreSQLTestContainerExtension;
import com.algaworks.algashop.billing.utility.tag.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ActiveProfiles("test")
@IntegrationTest
@SpringBootTest
@Import({FastpayCreditCardTokenizationAPIClientConfig.class})
@EnableWireMock(
        {
                @ConfigureWireMock(
                        name = "rapiDexApi",
                        port = 8788,
                        filesUnderDirectory = "src/test/resources/wiremock/fastpay",
                        globalTemplating = true
                )

        }
)
@ExtendWith(PostgreSQLTestContainerExtension.class)
class CreditCardProviderServiceFastpayImplTest extends AbstractFastpayTest {

    @PGContainer
    private static PostgreSQLContainer postgreSQLContainer;

    @Autowired
    CreditCardProviderServiceFastpayImplTest(final CreditCardProviderServiceFastpayImpl externalService,
                                             final FastpayCreditCardTokenizationAPIClient tokenizationAPIClient) {
        super(tokenizationAPIClient, externalService);
    }

    @DynamicPropertySource
    public static void configurePropertySource(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.flyway.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.flyway.user", postgreSQLContainer::getUsername);
        registry.add("spring.flyway.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    @Override
    public void setup() {
        super.setup();
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
        assertThatCode(() -> externalService.delete(limitedCreditCard.getGatewayCode()))
                .doesNotThrowAnyException();
    }

}