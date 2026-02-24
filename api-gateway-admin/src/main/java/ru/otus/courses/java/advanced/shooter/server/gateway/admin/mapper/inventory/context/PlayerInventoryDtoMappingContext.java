package ru.otus.courses.java.advanced.shooter.server.gateway.admin.mapper.inventory.context;

import lombok.Builder;
import org.mapstruct.Context;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AmmunitionDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.AttachmentDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GrenadeDto;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.dto.equipment.GunDto;

import java.util.Map;

@Builder
public record PlayerInventoryDtoMappingContext(
        @Context Map<Integer, GrenadeDto> grenadesById,
        @Context Map<Integer, AmmunitionDto> ammunitionById,
        @Context Map<Integer, AttachmentDto> attachmentsById,
        @Context Map<Integer, GunDto> gunsById
) {

}
