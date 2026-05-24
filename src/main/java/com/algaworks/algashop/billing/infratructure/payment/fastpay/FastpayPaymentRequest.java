package com.algaworks.algashop.billing.infratructure.payment.fastpay;

import lombok.Builder;
import lombok.Data;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

@Data
@Builder
public class FastpayPaymentRequest {

    private String referenceCode;
    private BigDecimal totalAmount;
    private String method;
    private String creditCardId;
    private String fullName;
    private String document;
    private String phone;
    private String addressLine1;
    @Nullable
    private String addressLine2;
    private String zipCode;
    private String replyToUrl;

}
