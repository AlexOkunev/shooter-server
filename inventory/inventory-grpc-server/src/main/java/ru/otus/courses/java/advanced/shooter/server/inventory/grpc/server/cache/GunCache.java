package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GetGunRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GetGunsRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.gun.GunInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceClient;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceRequestBuilder;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceResponseMapper;

import java.util.List;
import java.util.Optional;

@Component
public class GunCache extends EquipmentCacheImplBase implements EquipmentCache {

    public GunCache(EquipmentServiceClient equipmentServiceClient,
                    EquipmentServiceRequestBuilder equipmentServiceRequestBuilder,
                    EquipmentServiceResponseMapper equipmentServiceResponseMapper) {
        super(equipmentServiceClient, equipmentServiceRequestBuilder, equipmentServiceResponseMapper, EquipmentType.GUN);
    }

    @Override
    protected Optional<Equipment> loadEquipmentFromServiceById(int id) {
        GetGunRequest request = equipmentServiceRequestBuilder.buildGetGunRequest(id);
        GunInfo response = equipmentServiceClient.getGun(request);
        return Optional.of(equipmentServiceResponseMapper.toEquipment(response));
    }

    @Override
    protected Pair<List<Equipment>, Long> loadDataPageFromService(int page, int size) {
        GetGunsRequest request = equipmentServiceRequestBuilder.buildGetGunsRequest(page, size);
        GunInfoListPage response = equipmentServiceClient.getGuns(request);
        List<Equipment> equipmentList = response.getDataList().stream()
                .map(equipmentServiceResponseMapper::toEquipment)
                .toList();
        return Pair.of(equipmentList, response.getTotalCount());
    }
}
