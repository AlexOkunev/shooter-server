package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoneyBundleTradeStatusMapper {

    @ValueMapping(target = "CREATED", source = "MBT_CREATED")
    @ValueMapping(target = "PAYMENT_SESSION_WAIT", source = "MBT_PAYMENT_SESSION_WAIT")
    @ValueMapping(target = "PAYMENT_SESSION_CREATED", source = "MBT_PAYMENT_SESSION_CREATED")
    @ValueMapping(target = "PAYMENT_PENDING", source = "MBT_PAYMENT_PENDING")
    @ValueMapping(target = "PAYMENT_DONE", source = "MBT_PAYMENT_DONE")
    @ValueMapping(target = "MONEY_ISSUE_WAIT", source = "MBT_MONEY_ISSUE_WAIT")
    @ValueMapping(target = "MONEY_ISSUE_PENDING", source = "MBT_MONEY_ISSUE_PENDING")
    @ValueMapping(target = "MONEY_ISSUE_DONE", source = "MBT_MONEY_ISSUE_DONE")
    @ValueMapping(target = "SUCCEEDED", source = "MBT_SUCCEEDED")
    @ValueMapping(target = "FAILED", source = "MBT_FAILED")
    @ValueMapping(target = MappingConstants.THROW_EXCEPTION, source = "UNRECOGNIZED")
    MoneyBundleTradeStatus toEntity(ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.MoneyBundleTradeStatus source);

    List<MoneyBundleTradeStatus> toEntityList(List<ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.MoneyBundleTradeStatus> source);

    @InheritInverseConfiguration
    @ValueMapping(target = "UNRECOGNIZED", source = "UNKNOWN")
    ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.MoneyBundleTradeStatus toResponse(MoneyBundleTradeStatus source);
}
