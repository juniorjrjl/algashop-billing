package com.algaworks.algashop.billing.infratructure.payment.fastpay;

import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardNotFoundException;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardRepository;
import com.algaworks.algashop.billing.domain.model.invoice.payment.Payment;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentGatewayService;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentRequest;
import com.algaworks.algashop.billing.infratructure.payment.AlgashopPaymentProperties;
import com.algaworks.algashop.billing.presentation.BadGatewayException;
import com.algaworks.algashop.billing.presentation.GatewayTimeoutException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

import static com.algaworks.algashop.billing.infratructure.payment.fastpay.FastpayPaymentMethod.CREDIT;
import static com.algaworks.algashop.billing.infratructure.payment.fastpay.FastpayPaymentMethod.GATEWAY_BALANCE;
import static java.util.Objects.requireNonNull;

@Service
@ConditionalOnProperty(name = "algashop.integrations.payment.provider", havingValue = "FASTPAY")
@RequiredArgsConstructor
public class PaymentGatewayServiceFastpayImpl implements PaymentGatewayService {

    private final FastpayPaymentAPIClient fastpayPaymentAPIClient;
    private final CreditCardRepository creditCardRepository;
    private final AlgashopPaymentProperties algashopPaymentProperties;

    @Override
    public Payment capture(final PaymentRequest request) {
        final var fastpayRequest = toRequest(request);
        final FastpayPaymentResponse response;
        try {
            response = fastpayPaymentAPIClient.capture(fastpayRequest);
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException("Fastpay API Timeout", e);
        } catch (HttpClientErrorException e) {
            throw new BadGatewayException("Fastpay API Bad Gateway", e);
        }
        return toPayment(response);
    }

    @Override
    public Payment findByCode(final String gatewayCode) {
        final FastpayPaymentResponse response;
        try {
            response = fastpayPaymentAPIClient.findById(gatewayCode);
        } catch (ResourceAccessException e) {
            throw new GatewayTimeoutException("Fastpay API Timeout", e);
        } catch (HttpClientErrorException e) {
            throw new BadGatewayException("Fastpay API Bad Gateway", e);
        }
        return toPayment(response);
    }

    private FastpayPaymentRequest toRequest(final PaymentRequest request){
        final var payer = request.getPayer();
        final var address = payer.getAddress();
        final var builder = FastpayPaymentRequest.builder()
                .totalAmount(request.getAmount())
                .referenceCode(request.getInvoiceId().toString())
                .fullName(payer.getFullName())
                .document(payer.getDocument())
                .phone(payer.getPhone())
                .zipCode(address.getZipCode())
                .addressLine1(address.getStreet() + ", " + address.getNumber())
                .addressLine2(address.getComplement())
                .replyToUrl(algashopPaymentProperties.fastpay().webhookUrl());
        switch (request.getPaymentMethod()){
            case CREDIT_CARD -> {
                builder.method(CREDIT.name());
                final var creditCard = creditCardRepository.findById(
                        requireNonNull(request.getCreditCardId(), "Payment request without credit card")
                        )
                        .orElseThrow(CreditCardNotFoundException::new);
                builder.creditCardId(creditCard.getGatewayCode());
            }
            case GATEWAY_BALANCE -> builder.method(GATEWAY_BALANCE.name());
        }
        return builder.build();
    }

    private static Payment toPayment(final FastpayPaymentResponse response) {
        final var builder = Payment.builder()
                .gatewayCode(response.getId())
                .invoiceId(UUID.fromString(response.getReferenceCode()));
        final FastpayPaymentMethod fastpayPaymentMethod;
        try{
            fastpayPaymentMethod = FastpayPaymentMethod.valueOf(response.getMethod());
        }catch (Exception _){
            throw new IllegalStateException("Unknown Payment method: " + response.getMethod());
        }
        final FastpayPaymentStatus fastpayPaymentStatus;
        try{
            fastpayPaymentStatus = FastpayPaymentStatus.valueOf(response.getStatus());
        }catch (Exception _){
            throw new IllegalStateException("Unknown Payment status: " + response.getStatus());
        }
        return builder
                .status(FastpayEnumConverter.convert(fastpayPaymentStatus))
                .method(FastpayEnumConverter.convert(fastpayPaymentMethod))
                .build();
    }

}
