# Serviço Hub Pagamentos

## Fluxo de Pagamento:

```
Cliente
    │
    ▼
POST /payments
    │
    ▼
Persistir pagamento (PENDING)
    │
    ▼
Publicar mensagem RabbitMQ
    │
    ▼
Consumer
    │
    ▼
Simula processamento
    │
    ├── SUCCESS
    └── FAILED
    │
    ▼
Atualiza DB 
```

## Integrações futuras:

```
               PaymentGateway
                     ▲
                     │
        ┌────────────┴─────────────┐
        │                          │
FakeGateway               MercadoPagoGateway
        │                          │
        └────────────┬─────────────┘
                     │
                 StripeGateway

```

## Arquitetura Base

```
payment-hub

application
    usecases
    ports
    dto

domain
    entities
    enums
    exceptions
    services

infrastructure

    persistence
        mongo

    messaging
        rabbitmq

    gateway
        fake

    config

entrypoint

    rest

shared

```

## Tecnologias

|Tecnologia|Uso|
|---|---|
|Java 21||
|Spring Boot 3.5||
|Spring AMQP||
|RabbitMQ||
|MongoDB||
|Docker Compose||
|Spring Validation||
|MapStruct||
|Lombok||
|JUnit 5||
|Mockito||
|Testcontainers (Mongo + RabbitMQ)||
