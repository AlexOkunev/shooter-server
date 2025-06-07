package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

@Converter
public class ProductEquipmentTypeConverter implements AttributeConverter<ProductEquipmentType, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductEquipmentType productEquipmentType) {
        return productEquipmentType.getCode();
    }

    @Override
    public ProductEquipmentType convertToEntityAttribute(Integer code) {
        return ProductEquipmentType.fromCode(code);
    }
}
