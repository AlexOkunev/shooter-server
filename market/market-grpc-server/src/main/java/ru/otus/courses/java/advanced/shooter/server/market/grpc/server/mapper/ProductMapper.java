package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.PriceInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductEquipmentInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductWritableData;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ProductEquipmentTypeMapper.class})
public interface ProductMapper {
    @ConvertTimestampsToMs
    @Mappings({
            @Mapping(target = "equipment", source = ".", qualifiedByName = "getProductEquipmentInfo"),
            @Mapping(target = "price", source = ".", qualifiedByName = "getPriceInfo")
    })
    ProductInfo toResponse(Product source);

    @Named("getPriceInfo")
    @Mappings({
            @Mapping(target = "currencyId", source = "priceCurrency.id"),
            @Mapping(target = "amount", source = "price")
    })
    PriceInfo getPriceInfo(Product source);

    @Named("getProductEquipmentInfo")
    @Mappings({
            @Mapping(target = "id", source = "equipment.equipmentId"),
            @Mapping(target = "amount", source = "equipmentAmount"),
            @Mapping(target = "type", source = "equipment.equipmentType")
    })
    ProductEquipmentInfo getProductEquipmentInfo(Product source);

    @Mappings({
            @Mapping(target = Product.Fields.priceCurrency, source = "currency"),
            @Mapping(target = Product.Fields.equipment, source = "equipment"),
            @Mapping(target = Product.Fields.id, ignore = true),
            @Mapping(target = Product.Fields.enabled, source = "source.enabled"),
            @Mapping(target = Product.Fields.price, source = "source.price.amount"),
            @Mapping(target = Product.Fields.equipmentAmount, source = "source.equipment.amount")
    })
    Product toEntity(ProductWritableData source, ReferenceCurrency currency, ReferenceEquipment equipment);

    @Mappings({
            @Mapping(target = Product.Fields.priceCurrency, source = "currency"),
            @Mapping(target = Product.Fields.equipment, source = "equipment"),
            @Mapping(target = Product.Fields.id, ignore = true),
            @Mapping(target = Product.Fields.enabled, source = "source.enabled"),
            @Mapping(target = Product.Fields.price, source = "source.price.amount"),
            @Mapping(target = Product.Fields.equipmentAmount, source = "source.equipment.amount")
    })
    void update(@MappingTarget Product target, ProductWritableData source, ReferenceCurrency currency, ReferenceEquipment equipment);
}