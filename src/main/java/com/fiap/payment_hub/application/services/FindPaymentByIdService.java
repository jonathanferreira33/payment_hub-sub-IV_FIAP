package com.fiap.payment_hub.application.services;

import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.application.mappers.PaymentAppMapper;
import com.fiap.payment_hub.application.ports.input.FindPaymentByIdUseCase;
import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.exceptions.PaymentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FindPaymentByIdService implements FindPaymentByIdUseCase {

    private final PaymentRepository paymentRepository;

    public FindPaymentByIdService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentResponse execute(UUID id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Pagamento não encontrado com ID: "+ id));

        return PaymentAppMapper.domainToResponse(payment);
    }
}
