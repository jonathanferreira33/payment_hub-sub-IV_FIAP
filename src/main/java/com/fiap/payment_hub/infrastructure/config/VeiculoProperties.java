package com.fiap.payment_hub.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "integracao.gerenciamento")
public record VeiculoProperties (
        String baseUrl
) {
}
