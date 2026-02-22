package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.equipment;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.EquipmentType;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class EquipmentTypeMapper {

    public abstract EquipmentType toDto(ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType equipmentType);

    public abstract ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType toProto(EquipmentType equipmentType);
}
