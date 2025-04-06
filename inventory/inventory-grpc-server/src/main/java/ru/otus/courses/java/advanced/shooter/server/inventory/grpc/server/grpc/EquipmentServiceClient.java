package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.*;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.*;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.*;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.*;

//TODO!!! use resilience
@Slf4j
@Component
@RequiredArgsConstructor
public class EquipmentServiceClient {

    private final GrenadeServiceAPIGrpc.GrenadeServiceAPIBlockingStub equipmentServiceGrenadeBlockingStub;

    private final GunServiceAPIGrpc.GunServiceAPIBlockingStub equipmentServiceGunBlockingStub;

    private final AmmunitionServiceAPIGrpc.AmmunitionServiceAPIBlockingStub equipmentServiceAmmunitionBlockingStub;

    private final AttachmentServiceAPIGrpc.AttachmentServiceAPIBlockingStub equipmentServiceAttachmentBlockingStub;

    public GrenadeInfo getGrenade(GetGrenadeRequest getGrenadeRequest) {
        return equipmentServiceGrenadeBlockingStub.getGrenade(getGrenadeRequest);
    }

    public GrenadeInfoListPage getGrenades(GetGrenadesRequest getGrenadeRequest) {
        return equipmentServiceGrenadeBlockingStub.getGrenades(getGrenadeRequest);
    }

    public GunInfo getGun(GetGunRequest getGunRequest) {
        return equipmentServiceGunBlockingStub.getGun(getGunRequest);
    }

    public GunInfoListPage getGuns(GetGunsRequest getGunsRequest) {
        return equipmentServiceGunBlockingStub.getGuns(getGunsRequest);
    }

    public AmmunitionInfo getAmmunition(GetAmmunitionRequest getAmmunitionRequest) {
        return equipmentServiceAmmunitionBlockingStub.getAmmunition(getAmmunitionRequest);
    }

    public AmmunitionInfoListPage getAmmunitionList(GetAmmunitionListRequest getAmmunitionListRequest) {
        return equipmentServiceAmmunitionBlockingStub.getAmmunitionList(getAmmunitionListRequest);
    }

    public AttachmentInfo getAttachment(GetAttachmentRequest getAttachmentRequest) {
        return equipmentServiceAttachmentBlockingStub.getAttachment(getAttachmentRequest);
    }

    public AttachmentInfoListPage getAttachments(GetAttachmentsRequest getAttachmentsRequest) {
        return equipmentServiceAttachmentBlockingStub.getAttachments(getAttachmentsRequest);
    }
}
