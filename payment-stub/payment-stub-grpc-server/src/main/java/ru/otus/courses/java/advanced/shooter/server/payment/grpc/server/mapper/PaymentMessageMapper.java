package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.PaymentProcessedMessage;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMessageMapper {

    @Mappings({
            @Mapping(target = PaymentProcessedMessage.Fields.id, ignore = true),
            @Mapping(target = PaymentProcessedMessage.Fields.createdTimestamp, ignore = true),
            @Mapping(target = PaymentProcessedMessage.Fields.messageUuid, expression = "java(java.util.UUID.randomUUID())"),
    })
    PaymentProcessedMessage toEntity(Payment payment);
}
