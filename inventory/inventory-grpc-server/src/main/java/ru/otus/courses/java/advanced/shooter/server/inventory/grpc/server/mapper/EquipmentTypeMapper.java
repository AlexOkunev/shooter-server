package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EquipmentTypeMapper {

    @ValueMapping(target = "UNKNOWN", source = "UNRECOGNIZED")
    @ValueMapping(target = "UNKNOWN", source = "UNKNOWN")
    @ValueMapping(target = "GUN", source = "GUN")
    @ValueMapping(target = "GRENADE", source = "GRENADE")
    @ValueMapping(target = "ATTACHMENT", source = "ATTACHMENT")
    @ValueMapping(target = "AMMUNITION", source = "AMMUNITION")
    InventoryEquipmentType toEntity(EquipmentType source);

    @InheritInverseConfiguration
    EquipmentType toResponse(InventoryEquipmentType source);
}
