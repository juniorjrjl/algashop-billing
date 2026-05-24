package com.algaworks.algashop.billing.infratructure.payment.fastpay;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FastpayPaymentResponse {

    private String id;
    private String referenceCode;
    private String status;
    private String method;
    private BigDecimal totalAmount;

}
