package com.fiap.payment_hub.infrastructure.adapters.input.web.controller;

import com.fiap.payment_hub.application.dto.request.PaymentRequest;
import com.fiap.payment_hub.application.ports.input.CreatePaymentUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);


    public PaymentController(CreatePaymentUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<Void> criarPagamento(@RequestBody PaymentRequest request) {
        log.info("Recebido no Payment Hub: valor = {}", request.amount());
        useCase.execute(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
