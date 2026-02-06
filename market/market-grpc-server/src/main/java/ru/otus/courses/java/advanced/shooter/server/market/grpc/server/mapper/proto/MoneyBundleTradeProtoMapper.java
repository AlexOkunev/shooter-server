package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTradePayment;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.GetMoneyBundleTradesRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfo;

import java.util.List;
import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                MoneyBundleTradeStatusProtoMapper.class,
                DateMapper.class
        }
)
public abstract class MoneyBundleTradeProtoMapper {
    @Mappings({
            @Mapping(
                    target = "paymentInfo",
                    source = MoneyBundleTrade.Fields.payment
            )
    })
    public abstract MoneyBundleTradeInfo toResponse(MoneyBundleTrade source);

    public abstract MoneyBundleTradeInfo.PaymentInfo toResponse(MoneyBundleTradePayment source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<MoneyBundleTradeInfo> toResponseList(Iterable<MoneyBundleTrade> source);

    public Pageable toPageable(GetMoneyBundleTradesRequest request) {
        Sort sort = Sort.by(
                Sort.Order.asc(MoneyBundleTrade.Fields.playerUuid),
                Sort.Order.asc(MoneyBundleTrade.Fields.createdTimestamp)
        );

        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, sort);
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                sort
        );
    }

    public MoneyBundleTradeFilterParams toFilterParams(GetMoneyBundleTradesRequest request) {
        if (!request.hasFilter()) {
            return MoneyBundleTradeFilterParams.builder()
                    .playerUuid(UUID.fromString(request.getPlayerUuid()))
                    .build();
        }

        return toFilterParamsInternal(request.getFilter(), request.getPlayerUuid());
    }

    protected abstract MoneyBundleTradeFilterParams toFilterParamsInternal(GetMoneyBundleTradesRequest.Filter request, String playerUuid);
}