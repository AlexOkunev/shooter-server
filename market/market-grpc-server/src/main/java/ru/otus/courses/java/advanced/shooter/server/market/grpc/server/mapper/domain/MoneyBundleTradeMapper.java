package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUuid;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;

import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface MoneyBundleTradeMapper {

    @SetUuid
    @Mappings({
            @Mapping(
                    target = MoneyBundleTrade.Fields.status,
                    constant = "CREATED"
            ),
            @Mapping(
                    target = MoneyBundleTrade.Fields.moneyBundleId,
                    source = "moneyBundle.id"
            ),
            @Mapping(
                    target = MoneyBundleTrade.Fields.moneyBundle,
                    source = "moneyBundle"
            ),
            @Mapping(
                    target = MoneyBundleTrade.Fields.createdTimestamp,
                    ignore = true
            ),
            @Mapping(
                    target = MoneyBundleTrade.Fields.updatedTimestamp,
                    ignore = true
            ),
            @Mapping(
                    target = MoneyBundleTrade.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = MoneyBundleTrade.Fields.payment,
                    ignore = true
            )
    })
    MoneyBundleTrade toEntity(MoneyBundle moneyBundle, UUID playerUuid);
}