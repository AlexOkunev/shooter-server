package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryLogEntry;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.annotation.ConvertTimestampToMs;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.annotation.SetTimestamp;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {OperationTypeMapper.class, EquipmentTypeMapper.class})
public interface PlayerInventoryLogEntryMapper {
    @SetTimestamp
    @Mappings({
            @Mapping(target = PlayerInventoryLogEntry.Fields.operationType, expression = "java(ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType.ADMIN_GIVE)"),
            @Mapping(target = PlayerInventoryLogEntry.Fields.amountBefore, constant = "0"),
            @Mapping(target = PlayerInventoryLogEntry.Fields.amountAfter, source = PlayerInventoryItem.Fields.amount),
            @Mapping(target = PlayerInventoryLogEntry.Fields.heldAmountBefore, constant = "0"),
            @Mapping(target = PlayerInventoryLogEntry.Fields.heldAmountAfter, constant = "0")
    })
    PlayerInventoryLogEntry toEntityInitialize(PlayerInventoryItem source);

    @SetTimestamp
    @Mappings({
            @Mapping(target = PlayerInventoryLogEntry.Fields.amountBefore, source = PlayerInventoryItem.Fields.amount),
            @Mapping(target = PlayerInventoryLogEntry.Fields.heldAmountBefore, source = PlayerInventoryItem.Fields.heldAmount)
    })
    PlayerInventoryLogEntry toEntity(PlayerInventoryItem source);

    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(target = PlayerInventoryLogEntry.Fields.amountAfter, source = "source.amount"),
            @Mapping(target = PlayerInventoryLogEntry.Fields.heldAmountAfter, source = "source.heldAmount"),
            @Mapping(target = PlayerInventoryLogEntry.Fields.operationType, source = "operationType")
    })
    void update(@MappingTarget PlayerInventoryLogEntry target, PlayerInventoryItem source, OperationType operationType);

    @ConvertTimestampToMs
    ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogEntry toResponse(PlayerInventoryLogEntry source);
}
