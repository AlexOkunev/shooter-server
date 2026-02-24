package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.PlayerInventoryDtoMappingContext;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.page.PaginationInfoDtoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogEntry;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        uses = {
                DateMapper.class,
                PaginationInfoDtoMapper.class
        }
)
public abstract class PlayerInventoryLogWithDtoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract PlayerInventoryLogPageResponseDto toPageDto(PlayerInventoryLogPage playerInventoryLogPage,
                                                                @Context PlayerInventoryDtoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<PlayerInventoryLogEntryDto> toDtoList(Collection<PlayerInventoryLogEntry> logEntries,
                                                               @Context PlayerInventoryDtoMappingContext context);

    @Mapping(target = "equipment", source = ".")
    public abstract PlayerInventoryLogEntryDto toDto(PlayerInventoryLogEntry logEntry, @Context PlayerInventoryDtoMappingContext context);

    protected InventoryEquipmentDto toEquipmentDto(PlayerInventoryLogEntry itemInfo, @Context PlayerInventoryDtoMappingContext context) {
        if (itemInfo == null) {
            return null;
        }

        int equipmentId = itemInfo.getEquipmentId();

        return switch (itemInfo.getEquipmentType()) {
            case GUN -> new GunInventoryEquipmentDto(context.gunsById().get(equipmentId));
            case GRENADE -> new GrenadeInventoryEquipmentDto(context.grenadesById().get(equipmentId));
            case AMMUNITION -> new AmmunitionInventoryEquipmentDto(context.ammunitionById().get(equipmentId));
            case ATTACHMENT -> new AttachmentInventoryEquipmentDto(context.attachmentsById().get(equipmentId));
            default -> null;
        };
    }
}