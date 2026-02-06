package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;

import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerAccountItemMapper {
    @Mappings({
            @Mapping(
                    target = PlayerAccountItem.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerAccountItem.Fields.currency,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerAccountItem.Fields.amount,
                    constant = "0"
            )
    })
    PlayerAccountItem toEntityWithZeroAmount(UUID playerUuid, int currencyId);

    @Mappings({
            @Mapping(
                    target = PlayerAccountItem.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerAccountItem.Fields.currency,
                    ignore = true
            ),
    })
    PlayerAccountItem toEntity(InitialPlayerAccountItem source, UUID playerUuid);
}