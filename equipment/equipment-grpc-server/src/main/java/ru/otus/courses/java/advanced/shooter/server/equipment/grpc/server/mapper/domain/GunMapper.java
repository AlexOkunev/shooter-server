package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.CreateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GunSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Attachment;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                CommonMapper.class
        }
)
public abstract class GunMapper {
    private static final class SourceFields {
        static final String NAME = "source.name";
        static final String AMMUNITION_LIST = "ammunitionList";
        static final String ATTACHMENT_LIST = "attachmentList";
    }

    @CreateEntityMapping
    @Mappings({
            @Mapping(
                    source = SourceFields.AMMUNITION_LIST,
                    target = Gun.Fields.compatibleAmmunitionSet
            ),
            @Mapping(
                    source = SourceFields.ATTACHMENT_LIST,
                    target = Gun.Fields.compatibleAttachments
            ),
            @Mapping(
                    target = Currency.Fields.name,
                    source = SourceFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Gun.Fields.enabledCompatibleAmmunitionSet,
                    ignore = true
            ),
            @Mapping(
                    target = Gun.Fields.enabledCompatibleAttachments,
                    ignore = true
            )
    })
    public abstract Gun toEntity(GunSavedData source, List<Ammunition> ammunitionList, List<Attachment> attachmentList);

    @UpdateEntityMapping
    @Mappings({
            @Mapping(
                    source = SourceFields.AMMUNITION_LIST,
                    target = Gun.Fields.compatibleAmmunitionSet
            ),
            @Mapping(
                    source = SourceFields.ATTACHMENT_LIST,
                    target = Gun.Fields.compatibleAttachments
            ),
            @Mapping(
                    target = Gun.Fields.name,
                    source = SourceFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Gun.Fields.enabledCompatibleAmmunitionSet,
                    ignore = true
            ),
            @Mapping(
                    target = Gun.Fields.enabledCompatibleAttachments,
                    ignore = true
            )
    })
    public abstract void updateGun(@MappingTarget Gun gun, GunSavedData source, List<Ammunition> ammunitionList, List<Attachment> attachmentList);
}
