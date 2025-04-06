package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc;

import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;

@Component
public class EquipmentServiceResponseMapper {
    public Equipment toEquipment(GrenadeInfo grenadeInfo) {
        return new Equipment(grenadeInfo.getId(), grenadeInfo.getName(), grenadeInfo.getEnabled());
    }

    public Equipment toEquipment(GunInfo gunInfo) {
        return new Equipment(gunInfo.getId(), gunInfo.getName(), gunInfo.getEnabled());
    }

    public Equipment toEquipment(AttachmentInfo attachmentInfo) {
        return new Equipment(attachmentInfo.getId(), attachmentInfo.getName(), attachmentInfo.getEnabled());
    }

    public Equipment toEquipment(AmmunitionInfo ammunitionInfo) {
        return new Equipment(ammunitionInfo.getId(), ammunitionInfo.getName(), ammunitionInfo.getEnabled());
    }
}
