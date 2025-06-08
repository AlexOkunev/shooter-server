package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByTradeUuid(UUID tradeUuid);

    Page<Payment> findAllByProcessingFinishedTimestampBeforeAndStatus(ZonedDateTime time, PaymentStatus status, Pageable pageable);
}

