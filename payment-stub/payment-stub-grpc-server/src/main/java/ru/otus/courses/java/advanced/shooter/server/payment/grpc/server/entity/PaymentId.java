package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class PaymentId implements Serializable {
    private UUID playerUuid;
    private UUID paymentUuid;
}
