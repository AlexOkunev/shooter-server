package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValueMappingStrategy;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.AttachmentEffect;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class AttachmentEffectProtoMapper {

    public abstract ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentEffect toProto(AttachmentEffect source);

    public abstract AttachmentSavedData.AttachmentEffect toBean(ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentEffect source);
}
