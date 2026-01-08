package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.CreateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AttachmentSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.GunReducedMapper;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                GunReducedMapper.class,
                AttachmentEffectMapper.class,
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class AttachmentMapper {
    private static final class SourceFields {
        public static final String NAME = "source.name";
    }

    @CreateEntityMapping
    @Mappings({
            @Mapping(
                    target = Attachment.Fields.name,
                    source = SourceFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Attachment.Fields.enabledCompatibleGuns,
                    ignore = true
            )
    })
    public abstract Attachment toEntity(AttachmentSavedData source, List<Gun> compatibleGuns);

    @UpdateEntityMapping
    @Mappings({
            @Mapping(
                    target = Attachment.Fields.name,
                    source = SourceFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Attachment.Fields.enabledCompatibleGuns,
                    ignore = true
            )
    })
    public abstract void updateEntity(@MappingTarget Attachment target, AttachmentSavedData source, List<Gun> compatibleGuns);
}