package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUuid;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.outbox.PaymentProcessedMessage;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMessageMapper {

    @SetUuid
    PaymentProcessedMessage toEntity(Payment payment);
}
