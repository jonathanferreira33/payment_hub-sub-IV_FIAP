package com.fiap.payment_hub.infrastructure.adapters.output.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fiap.payment_hub.infrastructure.adapters.output.entity.JpaPaymentEntity;
import java.util.UUID;

public interface SpringDataPaymentRepository extends JpaRepository<JpaPaymentEntity, UUID> {
}