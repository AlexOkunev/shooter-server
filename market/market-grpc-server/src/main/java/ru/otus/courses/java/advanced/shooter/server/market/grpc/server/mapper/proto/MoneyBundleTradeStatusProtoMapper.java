package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.ThrowOnUnrecognized;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface MoneyBundleTradeStatusProtoMapper {

    @ThrowOnUnrecognized
    @ValueMapping(source = "MONEY_BUNDLE_TRADE_STATUS_UNSPECIFIED", target = MappingConstants.THROW_EXCEPTION)
    MoneyBundleTradeStatus toBean(ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeStatus source);

    ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeStatus toResponse(MoneyBundleTradeStatus source);
}
