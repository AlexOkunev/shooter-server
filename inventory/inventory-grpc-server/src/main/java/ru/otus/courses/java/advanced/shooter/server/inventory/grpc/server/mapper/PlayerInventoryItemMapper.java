package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerEquipmentOperationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemInfo;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {EquipmentTypeMapper.class})
public interface PlayerInventoryItemMapper {
    @Mappings({
            @Mapping(target = PlayerInventoryItem.Fields.amount, constant = "0"),
            @Mapping(target = PlayerInventoryItem.Fields.heldAmount, constant = "0")
    })
    PlayerInventoryItem toEntityWithZeroAmount(PlayerEquipmentOperationRequest source);

    @Mapping(target = PlayerInventoryItem.Fields.heldAmount, constant = "0")
    PlayerInventoryItem toEntity(InitialPlayerInventoryItem source, int playerId);

    PlayerInventoryItemInfo toResponse(PlayerInventoryItem source);
}
