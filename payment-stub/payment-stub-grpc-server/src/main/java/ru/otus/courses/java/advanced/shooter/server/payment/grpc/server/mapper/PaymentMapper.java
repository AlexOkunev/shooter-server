package ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.IgnoreCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.payment.grpc.server.entity.Payment;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;

import java.time.ZonedDateTime;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class
        }
)
public interface PaymentMapper {

    @IgnoreCreatedTimestamp
    @Mappings({
            @Mapping(target = Payment.Fields.paymentUuid, expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = Payment.Fields.paymentSession, source = "session"),
            @Mapping(target = Payment.Fields.publicToken, source = "token"),
            @Mapping(target = Payment.Fields.status, constant = "PROCESSING")
    })
    Payment toEntity(CreatePaymentRequest request, String session, String token, ZonedDateTime processingFinishedTimestamp);

    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    CreatePaymentResponse toResponse(Payment source);
}
