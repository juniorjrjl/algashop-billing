package com.algaworks.algashop.billing.utility.databuilder;

import com.algaworks.algashop.billing.utility.CustomFaker;

import java.util.List;
import java.util.stream.Stream;

public interface IDataBuilder<T> {

    CustomFaker customFaker = CustomFaker.getInstance();

    T build();

    default List<T> build(final long amount){
        return Stream.generate(this::build)
                .limit(amount)
                .toList();
    }

}
