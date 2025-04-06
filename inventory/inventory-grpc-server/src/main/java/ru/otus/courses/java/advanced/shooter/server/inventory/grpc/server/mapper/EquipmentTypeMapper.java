package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EquipmentTypeMapper {
    @ValueMapping(target = "UNKNOWN", source = "UNRECOGNIZED")
    @ValueMapping(target = "GUN", source = "GUN")
    @ValueMapping(target = "GRENADE", source = "GRENADE")
    @ValueMapping(target = "ATTACHMENT", source = "ATTACHMENT")
    @ValueMapping(target = "AMMUNITION", source = "AMMUNITION")
    EquipmentType toEntity(ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.EquipmentType source);

    @InheritInverseConfiguration
    ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.EquipmentType toResponse(EquipmentType source);
}
