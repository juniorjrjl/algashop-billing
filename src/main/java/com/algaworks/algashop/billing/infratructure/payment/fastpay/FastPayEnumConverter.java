package com.algaworks.algashop.billing.infratructure.payment.fastpay;

import com.algaworks.algashop.billing.domain.model.invoice.PaymentMethod;
import com.algaworks.algashop.billing.domain.model.invoice.payment.PaymentStatus;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class FastPayEnumConverter {

    public static PaymentMethod convert(final FastpayPaymentMethod method){
        return switch (method){
            case GATEWAY_BALANCE ->  PaymentMethod.GATEWAY_BALANCE;
            case CREDIT -> PaymentMethod.CREDIT_CARD;
        };
    }


    public static PaymentStatus convert(final FastpayPaymentStatus status){
        return switch (status){
            case PENDING -> PaymentStatus.PENDING;
            case PROCESSING -> PaymentStatus.PROCESSING;
            case FAILED, CANCELED -> PaymentStatus.FAILED;
            case PAID -> PaymentStatus.PAID;
            case REFUNDED -> PaymentStatus.REFUNDED;
        };
    }
}
