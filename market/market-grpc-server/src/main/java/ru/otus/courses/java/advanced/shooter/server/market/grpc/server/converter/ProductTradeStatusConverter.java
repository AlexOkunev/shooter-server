package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

@Converter
public class ProductTradeStatusConverter implements AttributeConverter<ProductTradeStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ProductTradeStatus productTradeStatus) {
        return productTradeStatus.getCode();
    }

    @Override
    public ProductTradeStatus convertToEntityAttribute(Integer code) {
        return ProductTradeStatus.fromCode(code);
    }
}
