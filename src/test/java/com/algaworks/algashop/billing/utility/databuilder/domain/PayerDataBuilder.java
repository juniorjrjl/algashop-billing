package com.algaworks.algashop.billing.utility.databuilder.domain;

import com.algaworks.algashop.billing.domain.model.invoice.Address;
import com.algaworks.algashop.billing.domain.model.invoice.Payer;
import com.algaworks.algashop.billing.utility.databuilder.IDataBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class PayerDataBuilder implements IDataBuilder<Payer> {

    private Supplier<String> fullName = () -> customFaker.name().fullName();
    private Supplier<String> document = () ->  customFaker.cpf().valid(false);
    private Supplier<String> phone = () -> customFaker.phoneNumber().cellPhone();
    private Supplier<String> email = () -> customFaker.internet().emailAddress();
    private Supplier<Address> address = () -> AddressDataBuilder.builder().build();

    public static PayerDataBuilder builder() {
        return new PayerDataBuilder();
    }

    @Override
    public Payer build() {
        return new Payer(
                fullName.get(),
                document.get(),
                phone.get(),
                email.get(),
                address.get()
        );
    }
}
