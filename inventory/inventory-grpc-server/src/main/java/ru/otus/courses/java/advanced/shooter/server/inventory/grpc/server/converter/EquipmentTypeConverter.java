package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;

@Converter
public class EquipmentTypeConverter implements AttributeConverter<EquipmentType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EquipmentType equipmentType) {
        return equipmentType.getCode();
    }

    @Override
    public EquipmentType convertToEntityAttribute(Integer code) {
        return EquipmentType.fromCode(code);
    }
}
