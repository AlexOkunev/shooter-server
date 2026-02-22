package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUuid;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountLogEntry;

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
public interface PlayerAccountLogEntryMapper {

    @SetTimestamp
    @SetUuid
    @Mappings({
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.operationType,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.amountBefore,
                    constant = "0"
            ),
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.amountAfter,
                    source = PlayerAccountItem.Fields.amount
            ),
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.operationUuid,
                    ignore = true
            )
    })
    PlayerAccountLogEntry toEntityForInitialize(PlayerAccountItem source);

    @SetTimestamp
    @SetUuid
    @Mappings({
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.amountBefore,
                    source = PlayerAccountItem.Fields.amount
            ),
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.amountAfter,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.operationType,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerAccountLogEntry.Fields.operationUuid,
                    ignore = true
            )
    })
    PlayerAccountLogEntry toEntity(PlayerAccountItem source);
}
