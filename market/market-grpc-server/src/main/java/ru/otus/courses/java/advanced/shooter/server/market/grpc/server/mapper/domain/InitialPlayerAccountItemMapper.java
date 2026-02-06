package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.SavedInitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;

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
public abstract class InitialPlayerAccountItemMapper {
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    @Mappings({
            @Mapping(
                    target = InitialPlayerAccountItem.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = InitialPlayerAccountItem.Fields.currency,
                    ignore = true
            )
    })
    public abstract InitialPlayerAccountItem toEntity(SavedInitialPlayerAccountItem source);

    @SetUpdatedTimestamp
    @Mappings({
            @Mapping(
                    target = InitialPlayerAccountItem.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = InitialPlayerAccountItem.Fields.createdTimestamp,
                    ignore = true
            ),
            @Mapping(
                    target = InitialPlayerAccountItem.Fields.currencyId,
                    ignore = true
            ),
            @Mapping(
                    target = InitialPlayerAccountItem.Fields.currency,
                    ignore = true
            )
    })
    public abstract void update(@MappingTarget InitialPlayerAccountItem target, SavedInitialPlayerAccountItem source);
}
