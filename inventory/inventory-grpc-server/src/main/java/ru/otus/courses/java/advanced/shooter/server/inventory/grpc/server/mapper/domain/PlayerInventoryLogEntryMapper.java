package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.domain;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.annotation.SetUuid;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.CommonMapper;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItemId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryLogEntry;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;

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
public interface PlayerInventoryLogEntryMapper {

    @SetTimestamp
    @SetUuid
    @Mappings({
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.operationType,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.amountBefore,
                    constant = "0"
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.amountAfter,
                    source = PlayerInventoryItem.Fields.amount
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.playerUuid,
                    source = PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.playerUuid
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.equipmentId,
                    source = PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentId
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.equipmentType,
                    source = PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentType
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.operationUuid,
                    ignore = true
            )
    })
    PlayerInventoryLogEntry toEntityForInitialize(PlayerInventoryItem source);

    @SetTimestamp
    @SetUuid
    @Mappings({
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.amountBefore,
                    source = PlayerInventoryItem.Fields.amount
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.amountAfter,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.operationType,
                    ignore = true
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.playerUuid,
                    source = PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.playerUuid
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.equipmentId,
                    source = PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentId
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.equipmentType,
                    source = PlayerInventoryItem.Fields.id + "." + PlayerInventoryItemId.Fields.referenceEquipmentId + "." + ReferenceEquipmentId.Fields.equipmentType
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.operationUuid,
                    ignore = true
            )
    })
    PlayerInventoryLogEntry toEntity(PlayerInventoryItem source);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.amountAfter,
                    source = "source.amount"
            ),
            @Mapping(
                    target = PlayerInventoryLogEntry.Fields.operationType,
                    source = "operationType"
            )
    })
    void update(@MappingTarget PlayerInventoryLogEntry target, PlayerInventoryItem source, OperationType operationType);
}
