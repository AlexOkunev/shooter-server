package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.ProductTradeInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ProductTradeStatusMapper.class, ProductEquipmentTypeMapper.class})
public interface ProductTradeMapper {
    @ConvertTimestampsToMs
    @Mappings({
            @Mapping(target = "tradeId", source = ProductTrade.Fields.id),
            @Mapping(target = "equipmentType", source = ProductTrade.Fields.productEquipmentType),
            @Mapping(target = "equipmentId", source = ProductTrade.Fields.productEquipmentId)
    })
    ProductTradeInfo toResponse(ProductTrade source);

    @Mappings({
            @Mapping(target = ProductTrade.Fields.status, constant = "CREATED"),
            @Mapping(target = ProductTrade.Fields.product, source = "product"),
            @Mapping(target = ProductTrade.Fields.productId, source = "product.id"),
            @Mapping(target = ProductTrade.Fields.priceValue, source = "product.price"),
            @Mapping(target = ProductTrade.Fields.productEquipment, source = "product.equipment"),
            @Mapping(target = ProductTrade.Fields.productEquipmentType, source = "product.equipmentType"),
            @Mapping(target = ProductTrade.Fields.productEquipmentId, source = "product.equipmentId"),
            @Mapping(target = ProductTrade.Fields.version, ignore = true),
            @Mapping(target = ProductTrade.Fields.uuid, expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = ProductTrade.Fields.id, ignore = true)
    })
    ProductTrade toEntity(int playerId, Product product);
}