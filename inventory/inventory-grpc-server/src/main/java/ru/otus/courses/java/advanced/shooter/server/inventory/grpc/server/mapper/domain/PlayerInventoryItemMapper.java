package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerEquipmentOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItemId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;

import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public abstract class PlayerInventoryItemMapper {

    @Mappings({
            @Mapping(
                    target = PlayerInventoryItem.Fields.id,
                    source = "."
            ),
            @Mapping(
                    target = PlayerInventoryItem.Fields.amount,
                    constant = "0"
            ),
            @Mapping(
                    target = PlayerInventoryItem.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerInventoryItem.Fields.referenceEquipment,
                    ignore = true
            )
    })
    public abstract PlayerInventoryItem toEntity(PlayerEquipmentOperationCommand source);

    @Mappings({
            @Mapping(
                    target = PlayerInventoryItem.Fields.id,
                    expression = "java(toEntityId(source, playerUuid))"
            ),
            @Mapping(
                    target = PlayerInventoryItem.Fields.version,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerInventoryItem.Fields.referenceEquipment,
                    ignore = true
            )
    })
    public abstract PlayerInventoryItem toEntity(InitialPlayerInventoryItem source, UUID playerUuid);

    @Mappings({
            @Mapping(
                    target = PlayerInventoryItemId.Fields.referenceEquipmentId,
                    source = "source.id"
            )
    })
    protected abstract PlayerInventoryItemId toEntityId(InitialPlayerInventoryItem source, UUID playerUuid);

    @Mappings({
            @Mapping(
                    target = PlayerInventoryItemId.Fields.referenceEquipmentId,
                    source = "."
            )
    })
    protected abstract PlayerInventoryItemId toEntityId(PlayerEquipmentOperationCommand source);

    protected abstract ReferenceEquipmentId toEquipmentId(PlayerEquipmentOperationCommand source);
}
