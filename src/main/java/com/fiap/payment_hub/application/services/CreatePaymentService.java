package com.fiap.payment_hub.application.services;

import com.fiap.payment_hub.application.dto.request.CardRequest;
import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.dto.request.PixRequest;
import com.fiap.payment_hub.application.dto.response.PaymentResponse;
import com.fiap.payment_hub.application.mappers.PaymentAppMapper;
import com.fiap.payment_hub.application.ports.input.CreatePaymentUseCase;
import com.fiap.payment_hub.application.ports.output.PaymentGateway;
import com.fiap.payment_hub.application.ports.output.PaymentRepository;
import com.fiap.payment_hub.domain.entities.Payment;
import com.fiap.payment_hub.domain.enums.PaymentStatus;
import com.fiap.payment_hub.domain.valueobjects.Card;
import com.fiap.payment_hub.domain.valueobjects.Pix;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePaymentService implements CreatePaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final AsyncPaymentProcessor asyncProcessor;

    public CreatePaymentService(
            PaymentRepository paymentRepository,
            AsyncPaymentProcessor asyncProcessor) {
        this.paymentRepository = paymentRepository;
        this.asyncProcessor = asyncProcessor;
    }

    @Override
    @Transactional
    public PaymentResponse execute(PaymentRequest request) {
        Card domainCard = mapToDomainCard(request.card());
        Pix domainPix = mapToDomainPix(request.pix());

        Payment payment = Payment.create(
                request.amount(),
                request.customerId(),
                request.description(),
                request.paymentMethod(),
                domainCard,
                domainPix,
                request.paymentCode()
        );

        Payment savedPayment = paymentRepository.save(payment);

        asyncProcessor.processAsynchronousPayment(payment.getPaymentCode());

        return PaymentAppMapper.domainToResponse(savedPayment);
    }

    private Card mapToDomainCard(CardRequest cardReq) {
        if (cardReq == null) return null;
        return new Card(cardReq.holderName(), cardReq.number(), cardReq.expiration(), cardReq.cvv(), cardReq.type());
    }

    private Pix mapToDomainPix(PixRequest pixReq) {
        if (pixReq == null) return null;
        return new Pix(pixReq.key(), pixReq.expiration());
    }
}
