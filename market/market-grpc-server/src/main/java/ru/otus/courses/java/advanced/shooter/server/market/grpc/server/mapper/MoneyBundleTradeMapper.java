package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.MoneyBundleTradeInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {MoneyBundleTradeStatusMapper.class, MoneyBundleTradePaymentMapper.class})
public interface MoneyBundleTradeMapper {
    @ConvertTimestampsToMs
    @Mappings({
            @Mapping(target = "tradeId", source = MoneyBundleTrade.Fields.id),
            @Mapping(target = "paymentInfo", source = MoneyBundleTrade.Fields.payment, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    })
    MoneyBundleTradeInfo toResponse(MoneyBundleTrade source);

    @Mappings({
            @Mapping(target = MoneyBundleTrade.Fields.status, constant = "CREATED"),
            @Mapping(target = MoneyBundleTrade.Fields.moneyBundle, source = "moneyBundle"),
            @Mapping(target = MoneyBundleTrade.Fields.moneyBundleId, source = "moneyBundle.id"),
            @Mapping(target = MoneyBundleTrade.Fields.version, ignore = true),
            @Mapping(target = MoneyBundleTrade.Fields.uuid, expression = "java(java.util.UUID.randomUUID())"),
            @Mapping(target = MoneyBundleTrade.Fields.id, ignore = true)
    })
    MoneyBundleTrade toEntity(int playerId, MoneyBundle moneyBundle);
}