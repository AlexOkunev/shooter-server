package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.CreateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipmentId;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class ProductMapper {

    @CreateEntityMapping
    @Mappings({
            @Mapping(
                    target = Product.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = Product.Fields.priceCurrency,
                    ignore = true
            ),
            @Mapping(
                    target = Product.Fields.equipment,
                    ignore = true
            ),
            @Mapping(
                    target = Product.Fields.equipmentId,
                    source = "."
            ),
            @Mapping(
                    target = Product.Fields.price,
                    source = ProductSavedData.Fields.priceCurrencyAmount
            )
    })
    public abstract Product toEntity(ProductSavedData data);

    @UpdateEntityMapping
    @Mappings({
            @Mapping(
                    target = Product.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = Product.Fields.priceCurrency,
                    ignore = true
            ),
            @Mapping(
                    target = Product.Fields.equipment,
                    ignore = true
            ),
            @Mapping(
                    target = Product.Fields.equipmentId,
                    source = "."
            ),
            @Mapping(
                    target = Product.Fields.price,
                    source = ProductSavedData.Fields.priceCurrencyAmount
            )
    })
    public abstract void update(@MappingTarget Product target, ProductSavedData data);

    protected abstract ReferenceEquipmentId toReferenceEquipmentId(ProductSavedData data);
}