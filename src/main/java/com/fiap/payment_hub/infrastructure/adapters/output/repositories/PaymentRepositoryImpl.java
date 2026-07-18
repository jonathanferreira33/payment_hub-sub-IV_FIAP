package com.fiap.payment_hub.infrastructure.adapters.output.repositories;

import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.infrastructure.adapters.output.entity.JpaPaymentEntity;
import com.fiap.payment_hub.infrastructure.adapters.output.mapper.PaymentInfrastructureMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final SpringDataPaymentRepository repository;

    public PaymentRepositoryImpl(SpringDataPaymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Payment save(Payment payment) {
        JpaPaymentEntity entity = PaymentInfrastructureMapper.domainToJpa(payment);
        JpaPaymentEntity savedEntity = repository.save(entity);
        return PaymentInfrastructureMapper.jpaToDomain(savedEntity);
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        return repository.findById(id)
                .map(PaymentInfrastructureMapper::jpaToDomain);
    }
}
