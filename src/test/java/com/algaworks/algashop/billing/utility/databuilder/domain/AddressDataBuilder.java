package com.algaworks.algashop.billing.utility.databuilder.domain;

import com.algaworks.algashop.billing.application.invoice.managment.AddressData;
import com.algaworks.algashop.billing.domain.model.invoice.Address;
import com.algaworks.algashop.billing.utility.databuilder.IDataBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
public class AddressDataBuilder implements IDataBuilder<Address> {

    @With
    private Supplier<String> street = () -> customFaker.address().streetAddress();
    @With
    private Supplier<String> number = () -> customFaker.address().buildingNumber();
    @With
    private Supplier<String> complement = () -> customFaker.address().secondaryAddress();
    @With
    private Supplier<String> neighborhood = () -> customFaker.address().cityName();
    @With
    private Supplier<String> city = () -> customFaker.address().city();
    @With
    private Supplier<String> state = () -> customFaker.address().state();
    @With
    private Supplier<String> zipCode = () -> customFaker.address().zipCode();

    public static AddressDataBuilder builder() {
        return new AddressDataBuilder();
    }

    @Override
    public Address build() {
        return new Address(
                street.get(),
                number.get(),
                complement.get(),
                neighborhood.get(),
                city.get(),
                state.get(),
                zipCode.get()
        );
    }

}
