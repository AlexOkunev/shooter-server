package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.outbox.PaymentProcessedMessage;

public interface PaymentProcessedMessageRepository extends JpaRepository<PaymentProcessedMessage, Integer> {
}
