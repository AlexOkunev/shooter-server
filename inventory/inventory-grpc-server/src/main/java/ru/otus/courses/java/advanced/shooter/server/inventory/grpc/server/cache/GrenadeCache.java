package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GetGrenadeRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GetGrenadesRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.grenade.GrenadeInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceClient;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceRequestBuilder;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceResponseMapper;

import java.util.List;
import java.util.Optional;

@Component
public class GrenadeCache extends EquipmentCacheImplBase implements EquipmentCache {

    public GrenadeCache(EquipmentServiceClient equipmentServiceClient,
                        EquipmentServiceRequestBuilder equipmentServiceRequestBuilder,
                        EquipmentServiceResponseMapper equipmentServiceResponseMapper) {
        super(equipmentServiceClient, equipmentServiceRequestBuilder, equipmentServiceResponseMapper, EquipmentType.GRENADE);
    }

    @Override
    protected Optional<Equipment> loadEquipmentFromServiceById(int id) {
        GetGrenadeRequest request = equipmentServiceRequestBuilder.buildGetGrenadeRequest(id);
        GrenadeInfo response = equipmentServiceClient.getGrenade(request);
        return Optional.of(equipmentServiceResponseMapper.toEquipment(response));
    }

    @Override
    protected Pair<List<Equipment>, Long> loadDataPageFromService(int page, int size) {
        GetGrenadesRequest request = equipmentServiceRequestBuilder.buildGetGrenadesRequest(page, size);
        GrenadeInfoListPage response = equipmentServiceClient.getGrenades(request);
        List<Equipment> equipmentList = response.getDataList().stream()
                .map(equipmentServiceResponseMapper::toEquipment)
                .toList();
        return Pair.of(equipmentList, response.getTotalCount());
    }
}
