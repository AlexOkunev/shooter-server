package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfo;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.AmmunitionInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.GetAmmunitionListRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.ammunition.GetAmmunitionRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceClient;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceRequestBuilder;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceResponseMapper;

import java.util.List;
import java.util.Optional;

@Component
public class AmmunitionCache extends EquipmentCacheImplBase implements EquipmentCache {

    public AmmunitionCache(EquipmentServiceClient equipmentServiceClient,
                           EquipmentServiceRequestBuilder equipmentServiceRequestBuilder,
                           EquipmentServiceResponseMapper equipmentServiceResponseMapper) {
        super(equipmentServiceClient, equipmentServiceRequestBuilder, equipmentServiceResponseMapper, EquipmentType.AMMUNITION);
    }

    @Override
    protected Optional<Equipment> loadEquipmentFromServiceById(int id) {
        GetAmmunitionRequest request = equipmentServiceRequestBuilder.buildGetAmmunitionRequest(id);
        AmmunitionInfo response = equipmentServiceClient.getAmmunition(request);
        return Optional.of(equipmentServiceResponseMapper.toEquipment(response));
    }

    @Override
    protected Pair<List<Equipment>, Long> loadDataPageFromService(int page, int size) {
        GetAmmunitionListRequest request = equipmentServiceRequestBuilder.buildGetAmmunitionListRequest(page, size);
        AmmunitionInfoListPage response = equipmentServiceClient.getAmmunitionList(request);
        List<Equipment> equipmentList = response.getDataList().stream()
                .map(equipmentServiceResponseMapper::toEquipment)
                .toList();
        return Pair.of(equipmentList, response.getTotalCount());
    }
}
