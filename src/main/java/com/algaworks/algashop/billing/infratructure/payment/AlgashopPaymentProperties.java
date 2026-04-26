package com.algaworks.algashop.billing.infratructure.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties("algashop.integrations.payment")
@Validated
public record AlgashopPaymentProperties(
        @NotNull
        AlgashopPaymentProvider provider,
        @NotNull
        FastPayProperties fastpay
) {

    public enum AlgashopPaymentProvider {
        FAKE,
        FASTPAY
    }

    @Validated
    public record FastPayProperties(
            @NotBlank
            String hostname,
            @NotBlank
            String privateToken

    ){}

}
