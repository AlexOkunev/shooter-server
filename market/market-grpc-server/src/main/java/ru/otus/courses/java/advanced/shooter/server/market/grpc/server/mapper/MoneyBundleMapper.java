package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleWritableData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoneyBundleMapper {
    @ConvertTimestampsToMs
    @Mapping(target = "currencyId", source = MoneyBundle.Fields.currency + "." + ReferenceCurrency.Fields.id)
    MoneyBundleInfo toResponse(MoneyBundle source);

    @Mappings({
            @Mapping(target = MoneyBundle.Fields.id, ignore = true),
            @Mapping(target = MoneyBundle.Fields.currency, source = "currency"),
            @Mapping(target = MoneyBundle.Fields.enabled, source = "source.enabled")
    })
    MoneyBundle toEntity(MoneyBundleWritableData source, ReferenceCurrency currency);

    @Mappings({
            @Mapping(target = MoneyBundle.Fields.id, ignore = true),
            @Mapping(target = MoneyBundle.Fields.enabled, source = "source.enabled"),
            @Mapping(target = MoneyBundle.Fields.currency, source = "currency")
    })
    void update(@MappingTarget MoneyBundle target, MoneyBundleWritableData source, ReferenceCurrency currency);
}
