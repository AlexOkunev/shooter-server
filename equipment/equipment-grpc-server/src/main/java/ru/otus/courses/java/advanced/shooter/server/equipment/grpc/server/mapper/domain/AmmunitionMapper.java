package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.CreateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.AmmunitionSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Ammunition;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Gun;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class AmmunitionMapper {
    private static final class SourceFields {
        public static final String NAME = "source.name";
    }

    @CreateEntityMapping
    @Mappings({
            @Mapping(
                    target = Ammunition.Fields.name,
                    source = SourceFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Ammunition.Fields.enabledCompatibleGuns,
                    ignore = true
            )
    })
    public abstract Ammunition toEntity(AmmunitionSavedData source, List<Gun> compatibleGuns);

    @UpdateEntityMapping
    @Mappings({
            @Mapping(
                    target = Ammunition.Fields.name,
                    source = SourceFields.NAME,
                    qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
            ),
            @Mapping(
                    target = Ammunition.Fields.enabledCompatibleGuns,
                    ignore = true
            )
    })
    public abstract void updateEntity(@MappingTarget Ammunition target, AmmunitionSavedData source, List<Gun> compatibleGuns);
}