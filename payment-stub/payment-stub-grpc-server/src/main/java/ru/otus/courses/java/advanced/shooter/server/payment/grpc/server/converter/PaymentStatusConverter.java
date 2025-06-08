package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.enumeration.PaymentStatus;

@Converter
public class PaymentStatusConverter implements AttributeConverter<PaymentStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PaymentStatus status) {
        return status.getCode();
    }

    @Override
    public PaymentStatus convertToEntityAttribute(Integer code) {
        return PaymentStatus.getByCode(code);
    }
}
