package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.IgnoreCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.IgnoreUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUuid;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;

import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface ProductTradeMapper {

    @SetUuid
    @IgnoreCreatedTimestamp
    @IgnoreUpdatedTimestamp
    @Mappings({
            @Mapping(
                    target = ProductTrade.Fields.status,
                    constant = "CREATED"
            ),
            @Mapping(
                    target = ProductTrade.Fields.product,
                    ignore = true
            ),
            @Mapping(
                    target = ProductTrade.Fields.productId,
                    source = "product.id"
            ),
            @Mapping(
                    target = ProductTrade.Fields.productEquipment,
                    ignore = true
            ),
            @Mapping(
                    target = ProductTrade.Fields.productEquipmentId,
                    source = "product.equipmentId"
            ),
            @Mapping(
                    target = ProductTrade.Fields.priceValue,
                    source = "product.price"
            ),
            @Mapping(
                    target = ProductTrade.Fields.version,
                    ignore = true
            ),
    })
    ProductTrade toEntity(UUID playerUuid, Product product);
}