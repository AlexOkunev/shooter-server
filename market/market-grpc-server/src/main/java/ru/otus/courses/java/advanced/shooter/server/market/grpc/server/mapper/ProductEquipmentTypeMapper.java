package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductEquipmentTypeMapper {

    @ValueMapping(target = "UNKNOWN", source = "UNRECOGNIZED")
    @ValueMapping(target = "UNKNOWN", source = "UNKNOWN")
    @ValueMapping(target = "GUN", source = "GUN")
    @ValueMapping(target = "GRENADE", source = "GRENADE")
    @ValueMapping(target = "ATTACHMENT", source = "ATTACHMENT")
    @ValueMapping(target = "AMMUNITION", source = "AMMUNITION")
    ProductEquipmentType toEntity(ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductEquipmentType source);

    List<ProductEquipmentType> toEntityList(List<ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductEquipmentType> source);

    @InheritInverseConfiguration
    ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductEquipmentType toResponse(ProductEquipmentType source);
}
