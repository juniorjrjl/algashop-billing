package com.algaworks.algashop.billing.domain.model.creditcard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {

    boolean existsByIdAndCustomerId(final UUID creditCardId, final UUID customerId);

    Optional<CreditCard> findByIdAndCustomerId(final UUID id, final UUID customerId);

    List<CreditCard> findAllByCustomerId(final UUID customerId);
}
