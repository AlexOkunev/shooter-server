package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.annotation.TrimName;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunWritableData;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {AttachmentReducedMapper.class, AmmunitionReducedMapper.class},
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE)
public interface GunMapper {
    @ConvertTimestampsToMs
    GunInfo toResponse(Gun source);

    @ConvertTimestampsToMs
    @Mapping(source = "compatibleAmmunitionList", target = "compatibleAmmunitionInfosList")
    @Mapping(source = "compatibleAttachments", target = "compatibleAttachmentsList")
    GunInfo toResponseWithRelatedEntities(Gun source);

    @ConvertTimestampsToMs
    @Mapping(source = "enabledCompatibleAmmunitionList", target = "compatibleAmmunitionInfosList")
    @Mapping(source = "enabledCompatibleAttachments", target = "compatibleAttachmentsList")
    GunInfo toResponseWithEnabledRelatedEntities(Gun source);

    @TrimName
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    @Mapping(source = "ammunitionList", target = "compatibleAmmunitionList")
    @Mapping(source = "attachmentList", target = "compatibleAttachments")
    Gun toEntity(GunWritableData source, List<Ammunition> ammunitionList, List<Attachment> attachmentList);

    @TrimName
    @SetUpdatedTimestamp
    @Mapping(source = "ammunitionList", target = "compatibleAmmunitionList")
    @Mapping(source = "attachmentList", target = "compatibleAttachments")
    void updateGun(@MappingTarget Gun gun, GunWritableData source, List<Ammunition> ammunitionList, List<Attachment> attachmentList);
}