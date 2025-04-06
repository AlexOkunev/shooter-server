package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.annotation.ConvertTimestampsToMs;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.annotation.SetCreatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.annotation.SetUpdatedTimestamp;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemInfo;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemRequest;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {EquipmentTypeMapper.class})
public interface InitialPlayerInventoryItemMapper {
    @SetCreatedTimestamp
    @SetUpdatedTimestamp
    @Mapping(target = InitialPlayerInventoryItem.Fields.version, ignore = true)
    InitialPlayerInventoryItem toEntity(InitialPlayerInventoryItemRequest source);

    @ConvertTimestampsToMs
    InitialPlayerInventoryItemInfo toResponse(InitialPlayerInventoryItem source);
}
