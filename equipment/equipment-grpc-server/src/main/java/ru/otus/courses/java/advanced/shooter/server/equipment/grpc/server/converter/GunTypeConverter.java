package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunType;

@Converter
public class GunTypeConverter implements AttributeConverter<GunType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(GunType gunType) {
        return gunType.getCode();
    }

    @Override
    public GunType convertToEntityAttribute(Integer code) {
        return GunType.fromCode(code);
    }
}
