package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;

@Converter
public class OperationTypeConverter implements AttributeConverter<OperationType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(OperationType equipmentType) {
        return equipmentType.getCode();
    }

    @Override
    public OperationType convertToEntityAttribute(Integer code) {
        return OperationType.fromCode(code);
    }
}
