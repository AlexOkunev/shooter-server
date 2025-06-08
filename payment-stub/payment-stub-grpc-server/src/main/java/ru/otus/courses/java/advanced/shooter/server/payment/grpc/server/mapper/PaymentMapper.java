package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper.annotation.ConvertCreatedTimestampToMs;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {
    @Mappings({
            @Mapping(target = Payment.Fields.id, ignore = true),
            @Mapping(target = Payment.Fields.processingFinishedTimestamp, ignore = true),
            @Mapping(target = Payment.Fields.paymentUuid, expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = Payment.Fields.paymentSession, source = "session"),
            @Mapping(target = Payment.Fields.publicToken, source = "token"),
            @Mapping(target = Payment.Fields.status, constant = "PROCESSING")
    })
    Payment toEntity(CreatePaymentRequest request, String session, String token);

    @ConvertCreatedTimestampToMs
    CreatePaymentResponse toResponse(Payment source);
}
