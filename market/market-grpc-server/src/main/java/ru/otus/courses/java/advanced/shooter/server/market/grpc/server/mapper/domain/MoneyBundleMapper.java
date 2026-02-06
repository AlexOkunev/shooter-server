package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.CreateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface MoneyBundleMapper {

    @CreateEntityMapping
    @Mappings({
            @Mapping(
                    target = MoneyBundle.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = MoneyBundle.Fields.currency,
                    ignore = true
            )
    })
    MoneyBundle toEntity(MoneyBundleSavedData data);

    @UpdateEntityMapping
    @Mappings({
            @Mapping(
                    target = MoneyBundle.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = MoneyBundle.Fields.currency,
                    ignore = true
            )
    })
    void update(@MappingTarget MoneyBundle target, MoneyBundleSavedData data);
}
