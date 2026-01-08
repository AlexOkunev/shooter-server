package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                GunReducedProtoMapper.class,
                AmmunitionReducedProtoMapper.class,
                GunTypeProtoMapper.class,
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class GunProtoMapper {
    private static final class ProtoFields {
        static final String COMPATIBLE_AMMUNITION_INFOS = "compatibleAmmunitionInfos";
        static final String COMPATIBLE_ATTACHMENTS = "compatibleAttachments";
    }

    private static class NamedMethods {
        static final String NAMED_TO_RESPONSE = "toResponse";
    }

    @IterableMapping(
            nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
            qualifiedByName = NamedMethods.NAMED_TO_RESPONSE
    )
    public abstract List<GunInfo> toResponseList(Iterable<Gun> source, @Context RelatedEntitiesInclusionMode mode);

    @Named(NamedMethods.NAMED_TO_RESPONSE)
    public GunInfo toResponse(Gun source, @Context RelatedEntitiesInclusionMode mode) {
        if (mode == null) {
            mode = RelatedEntitiesInclusionMode.DONT_INCLUDE;
        }

        return switch (mode) {
            case INCLUDE_ALL -> toResponseWithRelatedEntities(source);
            case INCLUDE_ONLY_ENABLED -> toResponseWithEnabledRelatedEntities(source);
            case DONT_INCLUDE -> toResponse(source);
            case UNRECOGNIZED -> throw new IllegalArgumentException("Unknown mode: " + mode);
        };
    }

    @Mappings({
            @Mapping(
                    target = ProtoFields.COMPATIBLE_AMMUNITION_INFOS,
                    ignore = true
            ),
            @Mapping(
                    target = ProtoFields.COMPATIBLE_ATTACHMENTS,
                    ignore = true
            )
    })
    public abstract GunInfo toResponse(Gun source);

    @Mappings({
            @Mapping(
                    source = Gun.Fields.compatibleAmmunitionSet,
                    target = ProtoFields.COMPATIBLE_AMMUNITION_INFOS
            ),
            @Mapping(
                    source = Gun.Fields.compatibleAttachments,
                    target = ProtoFields.COMPATIBLE_ATTACHMENTS
            )
    })
    public abstract GunInfo toResponseWithRelatedEntities(Gun source);

    @Mappings({
            @Mapping(
                    source = Gun.Fields.enabledCompatibleAmmunitionSet,
                    target = ProtoFields.COMPATIBLE_AMMUNITION_INFOS
            ),
            @Mapping(
                    source = Gun.Fields.enabledCompatibleAttachments,
                    target = ProtoFields.COMPATIBLE_ATTACHMENTS
            )
    })
    public abstract GunInfo toResponseWithEnabledRelatedEntities(Gun source);

    public GunSavedData toSavedData(CreateGunRequest createGunRequest) {
        if (!createGunRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(createGunRequest.getData());
    }

    public GunSavedData toSavedData(UpdateGunRequest updateGunRequest) {
        if (!updateGunRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(updateGunRequest.getData());
    }

    public GunFilterParams toFilterParams(GetGunsRequest request) {
        if (!request.hasFilter()) {
            return GunFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    public Pageable toPageable(GetGunsRequest request) {
        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"));
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    protected abstract GunSavedData toSavedDataInternal(GunWritableData source);

    protected abstract GunFilterParams toFilterParamsInternal(GunsFilter filter);
}
