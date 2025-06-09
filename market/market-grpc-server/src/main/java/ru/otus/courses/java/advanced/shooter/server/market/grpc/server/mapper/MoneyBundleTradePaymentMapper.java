package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.utils.mapping.MappingUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTradePayment;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.MoneyBundleTradeInfo;

import java.time.ZonedDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MoneyBundleTradePaymentMapper {
    @Mappings({
            @Mapping(target = "startTimestamp", source = MoneyBundleTradePayment.Fields.startTimestamp, qualifiedByName = "toMilliseconds"),
            @Mapping(target = "finishTimestamp", source = MoneyBundleTradePayment.Fields.finishTimestamp, qualifiedByName = "toMilliseconds",
                    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS),
    })
    MoneyBundleTradeInfo.PaymentInfo toResponse(MoneyBundleTradePayment source);

    @Named("toMilliseconds")
    static Long toMilliseconds(ZonedDateTime timestamp) {
        return MappingUtils.toMilliseconds(timestamp);
    }
}



