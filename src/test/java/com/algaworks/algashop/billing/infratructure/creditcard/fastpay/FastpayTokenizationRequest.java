package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FastpayTokenizationRequest {

    private String number;
    private String cvv;
    private String holderName;
    private String holderDocument;
    private Integer expMonth;
    private Integer expYear;

}
