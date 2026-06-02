package com.algaworks.algashop.billing.application.invoice.query;

import com.algaworks.algashop.billing.domain.model.invoice.InvoiceNotFoundException;
import com.algaworks.algashop.billing.domain.model.invoice.InvoiceRepository;
import com.algaworks.algashop.billing.utility.CustomFaker;
import com.algaworks.algashop.billing.utility.InvoiceDataBuilder;
import com.algaworks.algashop.billing.utility.extension.PGContainer;
import com.algaworks.algashop.billing.utility.extension.PostgreSQLTestContainerExtension;
import com.algaworks.algashop.billing.utility.tag.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ActiveProfiles("test")
@IntegrationTest
@SpringBootTest
@ExtendWith(PostgreSQLTestContainerExtension.class)
class InvoiceQueryServiceTest {

    private static final CustomFaker customFaker = CustomFaker.getInstance();

    private final InvoiceQueryService queryService;
    private final InvoiceRepository repository;

    @PGContainer
    private static PostgreSQLContainer postgreSQLContainer;

    @Autowired
    InvoiceQueryServiceTest(final InvoiceQueryService queryService,
                            final InvoiceRepository repository) {
        this.queryService = queryService;
        this.repository = repository;
    }

    @BeforeEach
    void setUp() {
        customFaker.reseed();
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

    @Test
    void shouldFindInvoiceByOrderId() {
        final var invoice = InvoiceDataBuilder.builder().buildIssue();
        repository.save(invoice);
        final var actual = queryService.findByOrderId(invoice.getOrderId());
        assertThat(actual.getOrderId()).isEqualTo(invoice.getOrderId());
    }

    @Test
    void giveNonStoredInvoiceIdWhenFindByOrderIdThenThrowException() {
        assertThatExceptionOfType(InvoiceNotFoundException.class)
                .isThrownBy(() -> queryService.findByOrderId(UUID.randomUUID().toString()));
    }

}