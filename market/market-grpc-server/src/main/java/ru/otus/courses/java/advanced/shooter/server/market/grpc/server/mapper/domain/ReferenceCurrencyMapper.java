package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SaveReferenceCurrencyCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class ReferenceCurrencyMapper {

    public abstract ReferenceCurrency toEntity(SaveReferenceCurrencyCommand command);
}
