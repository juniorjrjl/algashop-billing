package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FastpayTokenizedCreditCardResponse {

    private String tokenizedCard;
    private OffsetDateTime expiresAt;

}
