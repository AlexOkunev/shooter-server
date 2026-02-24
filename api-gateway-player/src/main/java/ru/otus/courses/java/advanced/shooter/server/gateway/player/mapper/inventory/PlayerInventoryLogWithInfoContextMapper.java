package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory;

import org.mapstruct.*;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.DateMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.inventory.*;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.AmmunitionMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.AttachmentMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GrenadeMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.equipment.GunMapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context.PlayerInventoryInfoMappingContext;
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
                PaginationInfoDtoMapper.class,
                GrenadeMapper.class,
                AmmunitionMapper.class,
                AttachmentMapper.class,
                GunMapper.class
        }
)
public abstract class PlayerInventoryLogWithInfoContextMapper {

    @Mapping(source = "data", target = "items")
    public abstract PlayerInventoryLogPageResponseDto toPageDto(PlayerInventoryLogPage playerInventoryLogPage,
                                                                @Context PlayerInventoryInfoMappingContext context);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    public abstract List<PlayerInventoryLogEntryDto> toDtoList(Collection<PlayerInventoryLogEntry> logEntries,
                                                               @Context PlayerInventoryInfoMappingContext context);

    @Mapping(target = "equipment", source = ".")
    public abstract PlayerInventoryLogEntryDto toDto(PlayerInventoryLogEntry logEntry, @Context PlayerInventoryInfoMappingContext context);

    protected InventoryEquipmentDto toEquipment(PlayerInventoryLogEntry logEntry, @Context PlayerInventoryInfoMappingContext context) {
        if (logEntry == null) {
            return null;
        }

        return switch (logEntry.getEquipmentType()) {
            case GUN -> toGunDto(context.gunsById().get(logEntry.getEquipmentId()));
            case GRENADE -> toGrenadeDto(context.grenadesById().get(logEntry.getEquipmentId()));
            case AMMUNITION -> toAmmunitionDto(context.ammunitionById().get(logEntry.getEquipmentId()));
            case ATTACHMENT -> toAttachmentDto(context.attachmentsById().get(logEntry.getEquipmentId()));
            default -> null;
        };
    }

    @Mapping(target = "grenade", source = ".")
    protected abstract GrenadeInventoryEquipmentDto toGrenadeDto(GrenadeInfo grenadeInfo);

    @Mapping(target = "ammunition", source = ".")
    protected abstract AmmunitionInventoryEquipmentDto toAmmunitionDto(AmmunitionInfo ammunitionInfo);

    @Mapping(target = "attachment", source = ".")
    protected abstract AttachmentInventoryEquipmentDto toAttachmentDto(AttachmentInfo ammunitionInfo);

    @Mapping(target = "gun", source = ".")
    protected abstract GunInventoryEquipmentDto toGunDto(GunInfo ammunitionInfo);
}