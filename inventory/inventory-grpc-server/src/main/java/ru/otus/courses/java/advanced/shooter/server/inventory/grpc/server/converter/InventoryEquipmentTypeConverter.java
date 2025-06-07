package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

@Converter
public class InventoryEquipmentTypeConverter implements AttributeConverter<InventoryEquipmentType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(InventoryEquipmentType inventoryEquipmentType) {
        return inventoryEquipmentType.getCode();
    }

    @Override
    public InventoryEquipmentType convertToEntityAttribute(Integer code) {
        return InventoryEquipmentType.fromCode(code);
    }
}
