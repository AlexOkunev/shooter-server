package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.OperationType;

@Converter
public class OperationTypeConverter implements AttributeConverter<OperationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OperationType attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public OperationType convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : OperationType.fromCode(dbData);
    }
}
