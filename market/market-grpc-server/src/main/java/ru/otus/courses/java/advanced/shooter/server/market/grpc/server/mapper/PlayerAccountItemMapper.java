package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PlayerAccountItemMapper {
    @Mappings({
            @Mapping(target = PlayerAccountItem.Fields.id, ignore = true),
            @Mapping(target = PlayerAccountItem.Fields.version, ignore = true),
            @Mapping(target = PlayerAccountItem.Fields.currencyAmount, constant = "0")
    })
    PlayerAccountItem toEntityWithZeroAmount(ReferenceCurrency currency, int playerId);

    @Mappings({
            @Mapping(target = PlayerAccountItem.Fields.id, ignore = true),
            @Mapping(target = PlayerAccountItem.Fields.version, ignore = true),
            @Mapping(target = PlayerAccountItem.Fields.currencyAmount, source = "source.amount")
    })
    PlayerAccountItem toEntity(InitialPlayerAccountItem source, int playerId);

    @Mappings({
            @Mapping(target = "amount", source = PlayerAccountItem.Fields.currencyAmount),
            @Mapping(target = "currencyId", source = PlayerAccountItem.Fields.currency + "." + ReferenceCurrency.Fields.id)
    })
    PlayerAccountItemInfo toResponse(PlayerAccountItem source);
}