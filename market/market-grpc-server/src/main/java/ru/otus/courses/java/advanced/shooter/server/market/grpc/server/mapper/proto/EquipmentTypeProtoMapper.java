package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.ThrowOnUnrecognized;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EquipmentTypeProtoMapper {

    @ThrowOnUnrecognized
    @ValueMapping(source = "EQUIPMENT_TYPE_UNSPECIFIED", target = MappingConstants.THROW_EXCEPTION)
    ProductEquipmentType toBean(EquipmentType equipmentType);

    EquipmentType toResponse(ProductEquipmentType source);
}
