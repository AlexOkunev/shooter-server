package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                GunReducedProtoMapper.class,
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class AmmunitionProtoMapper {

    private static class ProtoFields {
        static final String COMPATIBLE_GUNS = "compatibleGuns";
    }

    private static class NamedMethods {
        static final String NAMED_TO_RESPONSE = "toResponse";
    }

    @IterableMapping(
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
            qualifiedByName = NamedMethods.NAMED_TO_RESPONSE
    )
    public abstract List<AmmunitionInfo> toResponseList(Iterable<Ammunition> source, @Context RelatedEntitiesInclusionMode mode);

    @Named(NamedMethods.NAMED_TO_RESPONSE)
    public AmmunitionInfo toResponse(Ammunition source, @Context RelatedEntitiesInclusionMode mode) {
        if (mode == null) {
            mode = RelatedEntitiesInclusionMode.DONT_INCLUDE;
        }

        return switch (mode) {
            case INCLUDE_ALL -> toResponseWithGuns(source);
            case INCLUDE_ONLY_ENABLED -> toResponseWithEnabledGuns(source);
            case DONT_INCLUDE -> toResponse(source);
            case UNRECOGNIZED -> throw new IllegalArgumentException("Unknown mode: " + mode);
        };
    }

    @Mapping(
            target = ProtoFields.COMPATIBLE_GUNS,
            ignore = true
    )
    public abstract AmmunitionInfo toResponse(Ammunition source);

    @Mapping(
            source = Ammunition.Fields.compatibleGuns,
            target = ProtoFields.COMPATIBLE_GUNS
    )
    public abstract AmmunitionInfo toResponseWithGuns(Ammunition source);

    @Mapping(
            source = Ammunition.Fields.enabledCompatibleGuns,
            target = ProtoFields.COMPATIBLE_GUNS
    )
    public abstract AmmunitionInfo toResponseWithEnabledGuns(Ammunition source);

    public AmmunitionSavedData toSavedData(CreateAmmunitionRequest createAmmunitionRequest) {
        if (!createAmmunitionRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(createAmmunitionRequest.getData());
    }

    public AmmunitionSavedData toSavedData(UpdateAmmunitionRequest updateAmmunitionRequest) {
        if (!updateAmmunitionRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(updateAmmunitionRequest.getData());
    }

    public AmmunitionFilterParams toFilterParams(GetAmmunitionListRequest request) {
        if (!request.hasFilter()) {
            return AmmunitionFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    public Pageable toPageable(GetAmmunitionListRequest request) {
        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"));
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    protected abstract AmmunitionSavedData toSavedDataInternal(AmmunitionWritableData source);

    protected abstract AmmunitionFilterParams toFilterParamsInternal(AmmunitionFilter filter);
}