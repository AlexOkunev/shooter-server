package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.AttachmentInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.GetAttachmentRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.attachment.GetAttachmentsRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceClient;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceRequestBuilder;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceResponseMapper;

import java.util.List;
import java.util.Optional;

@Component
public class AttachmentCache extends EquipmentCacheImplBase implements EquipmentCache {

    public AttachmentCache(EquipmentServiceClient equipmentServiceClient,
                           EquipmentServiceRequestBuilder equipmentServiceRequestBuilder,
                           EquipmentServiceResponseMapper equipmentServiceResponseMapper) {
        super(equipmentServiceClient, equipmentServiceRequestBuilder, equipmentServiceResponseMapper, EquipmentType.ATTACHMENT);
    }

    @Override
    protected Optional<Equipment> loadEquipmentFromServiceById(int id) {
        GetAttachmentRequest request = equipmentServiceRequestBuilder.buildGetAttachmentRequest(id);
        AttachmentInfo response = equipmentServiceClient.getAttachment(request);
        return Optional.of(equipmentServiceResponseMapper.toEquipment(response));
    }

    @Override
    protected Pair<List<Equipment>, Long> loadDataPageFromService(int page, int size) {
        GetAttachmentsRequest request = equipmentServiceRequestBuilder.buildGetAttachmentsRequest(page, size);
        AttachmentInfoListPage response = equipmentServiceClient.getAttachments(request);
        List<Equipment> equipmentList = response.getDataList().stream()
                .map(equipmentServiceResponseMapper::toEquipment)
                .toList();
        return Pair.of(equipmentList, response.getTotalCount());
    }
}
