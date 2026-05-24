package com.algaworks.algashop.billing.infratructure.creditcard.fastpay;

import lombok.Data;

@Data
public class FastpayCreditCardResponse {

    private String id;
    private String lastNumbers;
    private String brand;
    private Integer expMonth;
    private Integer expYear;

}
