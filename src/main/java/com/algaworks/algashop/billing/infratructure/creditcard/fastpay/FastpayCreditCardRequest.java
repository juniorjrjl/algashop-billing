package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FastpayCreditCardRequest {

    private String tokenizedCard;
    private String customerCode;

}
