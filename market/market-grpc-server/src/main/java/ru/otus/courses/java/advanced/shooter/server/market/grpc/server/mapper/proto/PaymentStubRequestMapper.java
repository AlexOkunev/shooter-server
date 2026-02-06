package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import lombok.experimental.UtilityClass;
import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTradePayment;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentRequest;
import ru.otus.courses.java.advanced.shooter.server.payment.protobuf.CreatePaymentResponse;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class
        }
)
public abstract class PaymentStubRequestMapper {

    @UtilityClass
    private static class MappingNames {
        private static final String TARGET_TRADE_UUID = "tradeUuid";
        private static final String TARGET_RUBLES_AMOUNT = "rublesAmount";
        private static final String TARGET_PLAYER_EMAIL = "playerEmail";

        private static final String SOURCE_EMAIL = "email";
        private static final String SOURCE_TRADE_UUID = "trade.uuid";
        private static final String SOURCE_TRADE_RUBLES_PRICE = "trade.rublesPrice";
        private static final String SOURCE_SOURCE_PAYMENT_UUID = "source.paymentUuid";
        private static final String SOURCE_SOURCE_CREATED_TIMESTAMP = "source.createdTimestamp";
    }

    @Mappings({
            @Mapping(
                    target = MappingNames.TARGET_TRADE_UUID,
                    source = MappingNames.SOURCE_TRADE_UUID
            ),
            @Mapping(
                    target = MappingNames.TARGET_RUBLES_AMOUNT,
                    source = MappingNames.SOURCE_TRADE_RUBLES_PRICE
            ),
            @Mapping(
                    target = MappingNames.TARGET_PLAYER_EMAIL,
                    source = MappingNames.SOURCE_EMAIL
            )
    })
    public abstract CreatePaymentRequest toRequest(MoneyBundleTrade trade, String email);

    @Mappings({
            @Mapping(
                    target = MoneyBundleTradePayment.Fields.uuid,
                    source = MappingNames.SOURCE_SOURCE_PAYMENT_UUID
            ),
            @Mapping(
                    target = MoneyBundleTradePayment.Fields.startTimestamp,
                    source = MappingNames.SOURCE_SOURCE_CREATED_TIMESTAMP
            ),
            @Mapping(
                    target = MoneyBundleTradePayment.Fields.finishTimestamp,
                    ignore = true
            )
    })
    public abstract MoneyBundleTradePayment toPayment(CreatePaymentResponse source);
}
