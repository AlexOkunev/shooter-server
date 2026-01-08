package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.RelatedEntitiesInclusionMode;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.GunReducedMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                GunReducedMapper.class,
                CommonMapper.class,
                AttachmentEffectProtoMapper.class,
                AttachmentTypeProtoMapper.class
        }
)
public abstract class AttachmentProtoMapper {

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
    public abstract List<AttachmentInfo> toResponseList(Iterable<Attachment> source, @Context RelatedEntitiesInclusionMode mode);

    @Named(NamedMethods.NAMED_TO_RESPONSE)
    public AttachmentInfo toResponse(Attachment source, @Context RelatedEntitiesInclusionMode mode) {
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
    public abstract AttachmentInfo toResponse(Attachment source);

    @Mapping(
            source = Attachment.Fields.compatibleGuns,
            target = ProtoFields.COMPATIBLE_GUNS
    )
    public abstract AttachmentInfo toResponseWithGuns(Attachment source);

    @Mapping(
            source = Attachment.Fields.enabledCompatibleGuns,
            target = ProtoFields.COMPATIBLE_GUNS
    )
    public abstract AttachmentInfo toResponseWithEnabledGuns(Attachment source);

    public AttachmentSavedData toSavedData(CreateAttachmentRequest createAttachmentRequest) {
        if (!createAttachmentRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(createAttachmentRequest.getData());
    }

    public AttachmentSavedData toSavedData(UpdateAttachmentRequest updateAttachmentRequest) {
        if (!updateAttachmentRequest.hasData()) {
            return null;
        }

        return toSavedDataInternal(updateAttachmentRequest.getData());
    }

    public AttachmentFilterParams toFilterParams(GetAttachmentsRequest request) {
        if (!request.hasFilter()) {
            return AttachmentFilterParams.builder().build();
        }

        return toFilterParamsInternal(request.getFilter());
    }

    public Pageable toPageable(GetAttachmentsRequest request) {
        if (!request.hasPaginationRequest()) {
            return PageRequest.of(0, 20, Sort.by(Sort.Direction.ASC, "id"));
        }

        return PageRequest.of(
                request.getPaginationRequest().getPage(),
                request.getPaginationRequest().getCount(),
                Sort.by(Sort.Direction.ASC, "id")
        );
    }

    protected abstract AttachmentSavedData toSavedDataInternal(AttachmentWritableData source);

    protected abstract AttachmentFilterParams toFilterParamsInternal(AttachmentsFilter filter);
}