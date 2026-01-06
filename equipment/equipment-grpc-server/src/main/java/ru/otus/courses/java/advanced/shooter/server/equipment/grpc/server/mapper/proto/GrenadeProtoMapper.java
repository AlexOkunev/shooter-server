package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class GrenadeProtoMapper {
    private static final class ProtoFields {
        static final String NAME = "source.name";
    }

    public abstract GrenadeInfo toResponse(Grenade source);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<GrenadeInfo> toResponseList(Iterable<Grenade> source);

    public GrenadeSavedData toSavedData(CreateGrenadeRequest createGrenadeRequest) {
        if (!createGrenadeRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(createGrenadeRequest.getData());
    }

    public GrenadeSavedData toSavedData(UpdateGrenadeRequest updateGrenadeRequest) {
        if (!updateGrenadeRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(updateGrenadeRequest.getData());
    }

    public GrenadeFilterParams toFilterParams(GetGrenadesRequest request) {
        if (!request.hasFilter()) {
            return GrenadeFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    public Pageable toPageable(GetGrenadesRequest request) {
        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"));
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    protected abstract GrenadeSavedData toSavedDataInternal(GrenadeWritableData source);

    protected abstract GrenadeFilterParams toFilterParamsInternal(GrenadesFilter filter);
}
