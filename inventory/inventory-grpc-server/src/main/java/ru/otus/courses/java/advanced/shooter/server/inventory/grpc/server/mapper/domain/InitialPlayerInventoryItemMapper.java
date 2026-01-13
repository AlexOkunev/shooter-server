package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.UpdateEntityMapping;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.DeletedInitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.SavedInitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                CommonMapper.class
        }
)
public abstract class InitialPlayerInventoryItemMapper {
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    @Mappings({
            @Mapping(
                    target = InitialPlayerInventoryItem.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = InitialPlayerInventoryItem.Fields.id,
                    source = "."
            )
    })
    public abstract InitialPlayerInventoryItem toEntity(SavedInitialPlayerInventoryItem source);

    @UpdateEntityMapping
    @Mapping(
            target = InitialPlayerInventoryItem.Fields.version,
            ignore = true
    )
    public abstract void update(@MappingTarget InitialPlayerInventoryItem target, SavedInitialPlayerInventoryItem source);

    public abstract ReferenceEquipmentId toEntityId(SavedInitialPlayerInventoryItem source);

    public abstract ReferenceEquipmentId toEntityId(DeletedInitialPlayerInventoryItem source);
}
