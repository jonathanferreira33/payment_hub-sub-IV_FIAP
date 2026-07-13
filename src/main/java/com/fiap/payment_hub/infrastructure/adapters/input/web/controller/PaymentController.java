package com.fiap.payment_hub.infrastructure.adapters.input.web.controller;

import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.ports.input.CreatePaymentUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pagamentos")
public class PaymentController {

    private final CreatePaymentUseCase useCase;

    public PaymentController(CreatePaymentUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> criarPagamento(@RequestBody PaymentRequest request) {
        useCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
