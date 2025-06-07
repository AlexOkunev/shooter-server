package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

@Converter
public class MoneyBundleTradeStatusConverter implements AttributeConverter<MoneyBundleTradeStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MoneyBundleTradeStatus moneyBundleTradeStatus) {
        return moneyBundleTradeStatus.getCode();
    }

    @Override
    public MoneyBundleTradeStatus convertToEntityAttribute(Integer code) {
        return MoneyBundleTradeStatus.fromCode(code);
    }
}
