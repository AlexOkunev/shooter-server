package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class MoneyBundleProtoMapper {

    public abstract MoneyBundleInfo toResponse(MoneyBundle source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<MoneyBundleInfo> toResponseList(List<MoneyBundle> source);

    public MoneyBundleSavedData toSavedData(CreateMoneyBundleRequest request) {
        if (!request.hasData()) {
            return null;
        }

        return toSavedDataInternal(request.getData());
    }

    public MoneyBundleSavedData toSavedData(UpdateMoneyBundleRequest request) {
        if (!request.hasData()) {
            return null;
        }

        return toSavedDataInternal(request.getData());
    }


    public MoneyBundleFilterParams toFilterParams(GetMoneyBundlesRequest request) {
        if (!request.hasFilter()) {
            return MoneyBundleFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    public Pageable toPageable(GetMoneyBundlesRequest request) {
        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"));
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    protected abstract MoneyBundleSavedData toSavedDataInternal(MoneyBundleWritableData source);

    protected abstract MoneyBundleFilterParams toFilterParamsInternal(GetMoneyBundlesRequest.Filter filter);
}
