package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.CreateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencySavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class CurrencyMapper {
    @CreateEntityMapping
    @Mapping(
            target = Currency.Fields.name,
            source = CurrencySavedData.Fields.name,
            qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
    )
    public abstract Currency toEntity(CurrencySavedData source);

    @UpdateEntityMapping
    @Mapping(
            target = Currency.Fields.name,
            source = CurrencySavedData.Fields.name,
            qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
    )
    public abstract void updateCurrency(@MappingTarget Currency currency, CurrencySavedData source);
}