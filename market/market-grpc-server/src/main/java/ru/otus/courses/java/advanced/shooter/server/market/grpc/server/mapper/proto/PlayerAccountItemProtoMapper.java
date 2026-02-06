package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.PlayerCurrencyOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.GetPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerCurrencyOperationRequest;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class PlayerAccountItemProtoMapper {

    public abstract PlayerAccountItemInfo toResponse(PlayerAccountItem source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<PlayerAccountItemInfo> toResponseList(Iterable<PlayerAccountItem> source);

    public abstract PlayerCurrencyOperationCommand toCommand(PlayerCurrencyOperationRequest request);

    public Pageable toPageable(GetPlayerAccountRequest request) {
        Sort sort = Sort.by(Sort.Order.asc(PlayerAccountItem.Fields.currencyId));

        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, sort);
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                sort
        );
    }
}