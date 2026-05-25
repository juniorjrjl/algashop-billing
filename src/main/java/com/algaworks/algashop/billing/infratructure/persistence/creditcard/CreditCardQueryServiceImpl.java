package com.algaworks.algashop.billing.infratructure.persistence.creditcard;

import com.algaworks.algashop.billing.application.creditcard.query.CreditCardOutput;
import com.algaworks.algashop.billing.application.creditcard.query.CreditCardQueryService;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardNotFoundException;
import com.algaworks.algashop.billing.domain.model.creditcard.CreditCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreditCardQueryServiceImpl implements CreditCardQueryService {

    private final CreditCardRepository repository;
    private final CreditCardDisassembler disassembler;

    @Override
    public CreditCardOutput findOne(final UUID id, final UUID customerId) {
        return repository.findByIdAndCustomerId(id, customerId)
                .map(disassembler::toOutput)
                .orElseThrow(CreditCardNotFoundException::new);
    }

    @Override
    public List<CreditCardOutput> findByCustomer(final UUID customerId) {
        return repository.findAllByCustomerId(customerId).stream()
                .map(disassembler::toOutput)
                .toList();
    }
}
