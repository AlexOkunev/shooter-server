package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.ThrowOnUnrecognized;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductTradeStatusProtoMapper {

    @ThrowOnUnrecognized
    @ValueMapping(source = "PRODUCT_TRADE_STATUS_UNSPECIFIED", target = MappingConstants.THROW_EXCEPTION)
    ProductTradeStatus toBean(ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeStatus source);

    ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeStatus toResponse(ProductTradeStatus source);
}
