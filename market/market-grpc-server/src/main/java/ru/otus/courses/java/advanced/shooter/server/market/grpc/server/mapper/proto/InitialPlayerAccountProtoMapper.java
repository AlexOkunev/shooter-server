package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.InitialPlayerAccountFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.UpdateInitialPlayerAccountCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                EquipmentTypeProtoMapper.class,
                DateMapper.class
        }
)
public abstract class InitialPlayerAccountProtoMapper {
    public abstract UpdateInitialPlayerAccountCommand toCommand(UpdateInitialPlayerAccountRequest source);

    public abstract InitialPlayerAccountItemInfo toResponse(InitialPlayerAccountItem source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<InitialPlayerAccountItemInfo> toResponseList(Iterable<InitialPlayerAccountItem> source);

    public Pageable toPageable(GetInitialPlayerAccountRequest request) {
        Sort sort = Sort.by(InitialPlayerAccountItem.Fields.currencyId);

        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, sort);
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                sort
        );
    }

    public InitialPlayerAccountFilterParams toFilterParams(GetInitialPlayerAccountRequest request) {
        if (!request.hasFilter()) {
            return InitialPlayerAccountFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    protected abstract InitialPlayerAccountFilterParams toFilterParamsInternal(GetInitialPlayerAccountRequest.Filter request);
}
