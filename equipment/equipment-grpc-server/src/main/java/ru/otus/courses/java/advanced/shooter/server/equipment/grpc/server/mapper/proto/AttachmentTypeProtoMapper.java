package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.ThrowOnUnrecognized;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentType;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttachmentTypeProtoMapper {
    @ThrowOnUnrecognized
    @ValueMapping(source = "ATTACHMENT_TYPE_UNSPECIFIED", target = MappingConstants.THROW_EXCEPTION)
    AttachmentType toBean(
            ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentType proto
    );

    ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentType toProto(
            AttachmentType bean
    );
}
