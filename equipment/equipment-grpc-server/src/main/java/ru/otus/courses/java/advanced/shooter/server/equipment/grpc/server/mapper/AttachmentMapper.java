package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.AttachmentEffect;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.TrimName;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentWritableData;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {GunReducedMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface AttachmentMapper {

    @ConvertTimestampsToMs
    @Mapping(target = "compatibleGunsList", ignore = true)
    AttachmentInfo toResponse(Attachment source);

    @ConvertTimestampsToMs
    @Mapping(source = "compatibleGuns", target = "compatibleGunsList")
    AttachmentInfo toResponseWithGuns(Attachment source);

    @ConvertTimestampsToMs
    @Mapping(source = "enabledCompatibleGuns", target = "compatibleGunsList")
    AttachmentInfo toResponseWithEnabledGuns(Attachment source);

    @TrimName
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    Attachment toEntity(AttachmentWritableData source, List<Gun> compatibleGuns);

    @TrimName
    @SetUpdatedTimestamp
    void updateAttachment(@MappingTarget Attachment target, AttachmentWritableData source, List<Gun> compatibleGuns);

    AttachmentEffect toEntity(ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentEffect source);

    ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentEffect toResponse(AttachmentEffect source);
}