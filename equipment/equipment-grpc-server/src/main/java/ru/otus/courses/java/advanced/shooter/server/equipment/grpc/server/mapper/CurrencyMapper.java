package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.TrimName;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.CurrencyWritableData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CurrencyMapper {
    @ConvertTimestampsToMs
    CurrencyInfo toResponse(Currency source);

    @TrimName
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    Currency toEntity(CurrencyWritableData source);

    @TrimName
    @SetUpdatedTimestamp
    void updateCurrency(@MappingTarget Currency currency, CurrencyWritableData source);
}