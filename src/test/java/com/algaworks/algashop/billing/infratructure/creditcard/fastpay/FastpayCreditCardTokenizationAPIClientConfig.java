package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import com.algaworks.algashop.billing.infratructure.payment.AlgashopPaymentProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class FastpayCreditCardTokenizationAPIClientConfig {

    @Bean
    FastpayCreditCardTokenizationAPIClient fastpayCreditCardTokenizationAPIClient(final RestClient.Builder builder,
                                                                                  final AlgashopPaymentProperties properties,
                                                                                  @Value("${algashop.integrations.payment.fastpay.public-token}")
                                                                                  final String publicToken) {
        final var fastpayProperties = properties.fastpay();
        final var restClient = builder.baseUrl(fastpayProperties.hostname())
                .requestInterceptor(((request, body, execution) -> {
                    request.getHeaders().add("Token", publicToken);
                    return execution.execute(request, body);
                })).build();
        final var adapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
        return proxyFactory.createClient(FastpayCreditCardTokenizationAPIClient.class);
    }
}
