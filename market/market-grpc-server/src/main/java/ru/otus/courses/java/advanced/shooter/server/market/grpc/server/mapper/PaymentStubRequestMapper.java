package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.utils.mapping.MappingUtils;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTradePayment;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;

import java.time.ZonedDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentStubRequestMapper {
    @Mappings({
            @Mapping(target = "tradeUuid", source = "trade.uuid"),
            @Mapping(target = "rublesAmount", source = "trade.rublesPrice")
    })
    CreatePaymentRequest toRequest(MoneyBundleTrade trade, String playerEmail);

    @Mappings({
            @Mapping(target = MoneyBundleTradePayment.Fields.publicToken, source = "source.publicToken"),
            @Mapping(target = MoneyBundleTradePayment.Fields.session, source = "source.paymentSession"),
            @Mapping(target = MoneyBundleTradePayment.Fields.uuid, source = "source.paymentUuid"),
            @Mapping(target = MoneyBundleTradePayment.Fields.startTimestamp, source = "source.createdTimestamp", qualifiedByName = "toZonedDateTime")
    })
    MoneyBundleTradePayment toPayment(CreatePaymentResponse source);

    @Named("toZonedDateTime")
    static ZonedDateTime toZonedDateTime(long timestamp) {
        return MappingUtils.toZonedDateTime(timestamp);
    }
}
