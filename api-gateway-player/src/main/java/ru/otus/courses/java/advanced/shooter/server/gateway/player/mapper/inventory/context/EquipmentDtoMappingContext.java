package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context;

import lombok.Builder;
import org.mapstruct.Context;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.dto.equipment.GunDto;

import java.util.Map;

@Builder
public record EquipmentDtoMappingContext(
        @Context Map<Integer, GrenadeDto> grenadesById,
        @Context Map<Integer, AmmunitionDto> ammunitionById,
        @Context Map<Integer, AttachmentDto> attachmentsById,
        @Context Map<Integer, GunDto> gunsById
) {

}
