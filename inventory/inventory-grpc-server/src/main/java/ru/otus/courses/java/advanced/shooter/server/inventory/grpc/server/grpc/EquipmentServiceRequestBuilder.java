package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc;

import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.GetAmmunitionListRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.GetAmmunitionRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.GetAttachmentRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.GetAttachmentsRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.common.Common;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GetGrenadeRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GetGrenadesRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GetGunRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GetGunsRequest;

@Component
public class EquipmentServiceRequestBuilder {
    public GetGrenadeRequest buildGetGrenadeRequest(int id) {
        return GetGrenadeRequest.newBuilder()
                .setGrenadeId(id)
                .build();
    }

    public GetGrenadesRequest buildGetGrenadesRequest(int page, int count) {
        return GetGrenadesRequest.newBuilder()
                .setPaginationRequest(
                        Common.PaginationRequest.newBuilder()
                                .setCount(count)
                                .setPage(page)
                                .build())
                .build();
    }

    public GetGunRequest buildGetGunRequest(int id) {
        return GetGunRequest.newBuilder()
                .setGunId(id)
                .build();
    }

    public GetGunsRequest buildGetGunsRequest(int page, int count) {
        return GetGunsRequest.newBuilder()
                .setPaginationRequest(
                        Common.PaginationRequest.newBuilder()
                                .setCount(count)
                                .setPage(page)
                                .build())
                .setRelatedEntitiesInclusionMode(Common.RelatedEntitiesInclusionMode.DONT_INCLUDE)
                .build();
    }

    public GetAmmunitionRequest buildGetAmmunitionRequest(int id) {
        return GetAmmunitionRequest.newBuilder()
                .setAmmunitionId(id)
                .build();
    }

    public GetAmmunitionListRequest buildGetAmmunitionListRequest(int page, int count) {
        return GetAmmunitionListRequest.newBuilder()
                .setPaginationRequest(
                        Common.PaginationRequest.newBuilder()
                                .setCount(count)
                                .setPage(page)
                                .build())
                .setCompatibleGunsInclusionMode(Common.RelatedEntitiesInclusionMode.DONT_INCLUDE)
                .build();
    }

    public GetAttachmentRequest buildGetAttachmentRequest(int id) {
        return GetAttachmentRequest.newBuilder()
                .setAttachmentId(id)
                .build();
    }

    public GetAttachmentsRequest buildGetAttachmentsRequest(int page, int count) {
        return GetAttachmentsRequest.newBuilder()
                .setPaginationRequest(
                        Common.PaginationRequest.newBuilder()
                                .setCount(count)
                                .setPage(page)
                                .build())
                .setCompatibleGunsInclusionMode(Common.RelatedEntitiesInclusionMode.DONT_INCLUDE)
                .build();
    }
}
