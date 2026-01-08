package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.IgnoreId;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.AmmunitionReducedProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.AttachmentReducedProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunWritableData;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                AttachmentReducedProtoMapper.class,
                AmmunitionReducedProtoMapper.class,
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class GunMapper {
    private static final class ProtoFields {
        static final String NAME = "source.name";
        static final String COMPATIBLE_AMMUNITION_INFOS = "compatibleAmmunitionInfos";
        static final String COMPATIBLE_ATTACHMENTS = "compatibleAttachments";
        static final String AMMUNITION_LIST = "ammunitionList";
        static final String ATTACHMENT_LIST = "attachmentList";
    }

    @Mappings({
            @Mapping(
                    ignore = true,
                    target = ProtoFields.COMPATIBLE_AMMUNITION_INFOS
            ),
            @Mapping(
                    ignore = true,
                    target = ProtoFields.COMPATIBLE_ATTACHMENTS
            )
    })
    public abstract GunInfo toResponse(Gun source);

    @Mappings({
            @Mapping(
                    source = Gun.Fields.compatibleAmmunitionList,
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
                    source = Gun.Fields.enabledCompatibleAmmunitionList,
                    target = ProtoFields.COMPATIBLE_AMMUNITION_INFOS
            ),
            @Mapping(
                    source = Gun.Fields.enabledCompatibleAttachments,
                    target = ProtoFields.COMPATIBLE_ATTACHMENTS
            )
    })
    public abstract GunInfo toResponseWithEnabledRelatedEntities(Gun source);

    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    @IgnoreId
    @Mappings({
            @Mapping(
                    source = ProtoFields.AMMUNITION_LIST,
                    target = Gun.Fields.compatibleAmmunitionList
            ),
            @Mapping(
                    source = ProtoFields.ATTACHMENT_LIST,
                    target = Gun.Fields.compatibleAttachments
            ),
            @Mapping(
                    target = Currency.Fields.name,
                    source = ProtoFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Gun.Fields.enabledCompatibleAmmunitionList,
                    ignore = true
            ),
            @Mapping(
                    target = Gun.Fields.enabledCompatibleAttachments,
                    ignore = true
            ),
    })
    public abstract Gun toEntity(GunWritableData source, List<Ammunition> ammunitionList, List<Attachment> attachmentList);

    @SetUpdatedTimestamp
    @IgnoreId
    @Mappings({
            @Mapping(
                    source = ProtoFields.AMMUNITION_LIST,
                    target = Gun.Fields.compatibleAmmunitionList
            ),
            @Mapping(
                    source = ProtoFields.ATTACHMENT_LIST,
                    target = Gun.Fields.compatibleAttachments
            ),
            @Mapping(
                    target = Gun.Fields.name,
                    source = ProtoFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Gun.Fields.createdTimestamp,
                    ignore = true
            )
    })
    public abstract void updateGun(@MappingTarget Gun gun, GunWritableData source, List<Ammunition> ammunitionList, List<Attachment> attachmentList);
}

//TODO!!! fix /Users/alex/Documents/GitHub/shooter-server/equipment/equipment-grpc-server/src/main/java/ru/otus/courses/java/advanced/shooter/server/equipment/grpc/server/mapper/GunMapper.java:[124,26] Unmapped target properties: "enabledCompatibleAmmunitionList, enabledCompatibleAttachments".