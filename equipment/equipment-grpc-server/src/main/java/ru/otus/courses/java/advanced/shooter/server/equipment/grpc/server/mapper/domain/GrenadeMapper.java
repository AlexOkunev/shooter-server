package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.CreateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.GrenadeSavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Grenade;

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
public abstract class GrenadeMapper {
    @CreateEntityMapping
    @Mapping(
            target = Grenade.Fields.name,
            source = GrenadeSavedData.Fields.name,
            qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
    )
    public abstract Grenade toEntity(GrenadeSavedData source);

    @UpdateEntityMapping
    @Mapping(
            target = Grenade.Fields.name,
            source = GrenadeSavedData.Fields.name,
            qualifiedByName = CommonMapper.NamedMethods.NAMED_TRIM
    )
    public abstract void updateGrenade(@MappingTarget Grenade grenade, GrenadeSavedData source);
}
