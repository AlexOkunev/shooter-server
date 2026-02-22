package ru.otus.courses.java.advanced.shooter.server.gateway.player.mapper.inventory.context;

import lombok.Builder;
import org.mapstruct.Context;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;

import java.util.Map;

@Builder
public record PlayerInventoryMappingContext(
        @Context Map<Integer, GrenadeInfo> grenadesById,
        @Context Map<Integer, AmmunitionInfo> ammunitionById,
        @Context Map<Integer, AttachmentInfo> attachmentsById,
        @Context Map<Integer, GunInfo> gunsById
) {

}
