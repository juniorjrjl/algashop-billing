package com.algaworks.algashop.billing.application.invoice.managment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenerateInvoiceInput{
    @Nullable
    private String orderId;
    @NotNull
    private UUID customerId;
    @NotNull
    private PaymentSettingsInput paymentSettings;
    @NotNull
    @Valid
    private PayerData payer;
    @NotEmpty
    @Valid
    private Set<@NotNull LineItemInput> items;
}