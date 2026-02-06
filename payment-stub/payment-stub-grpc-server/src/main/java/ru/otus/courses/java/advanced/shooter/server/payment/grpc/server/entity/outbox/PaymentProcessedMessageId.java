package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.outbox;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentProcessedMessageId {

    private UUID playerUuid;

    private UUID uuid;
}
