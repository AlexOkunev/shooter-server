package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;

public interface PaymentService {
    CreatePaymentResponse createPayment(CreatePaymentRequest request);

    void processPaymentResult(Payment payment, PaymentStatus status);
}
