package com.algaworks.algashop.billing.infratructure.persistence.creditcard;

import com.algaworks.algashop.billing.application.creditcard.query.CreditCardOutput;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCard;
import org.jspecify.annotations.NullMarked;
import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@AnnotateWith(NullMarked.class)
@Mapper(componentModel = SPRING)
public interface CreditCardDisassembler {

    @Mapping(target = "expMonth", source = "expirationMonth")
    @Mapping(target = "expYear", source = "expirationYear")
    CreditCardOutput toOutput(final CreditCard creditCard);
}
