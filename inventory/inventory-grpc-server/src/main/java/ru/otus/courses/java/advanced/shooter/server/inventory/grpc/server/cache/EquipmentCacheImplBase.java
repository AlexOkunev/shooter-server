package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.domain.Equipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceClient;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceRequestBuilder;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.grpc.EquipmentServiceResponseMapper;
import ru.otus.courses.java.advanced.shooter.server.utils.cache.impl.SoftReferenceMapCache;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public abstract class EquipmentCacheImplBase implements EquipmentCache {
    private final SoftReferenceMapCache<Integer, Equipment> equipmentCache = new SoftReferenceMapCache<>(new ConcurrentHashMap<>());

    protected final EquipmentServiceClient equipmentServiceClient;

    protected final EquipmentServiceRequestBuilder equipmentServiceRequestBuilder;

    protected final EquipmentServiceResponseMapper equipmentServiceResponseMapper;

    protected final EquipmentType equipmentType;

    @Override
    public void put(Equipment equipment) {
        equipmentCache.put(equipment.id(), equipment);
    }

    @Override
    public Optional<Equipment> getById(int id) {
        Optional<Equipment> equipment = Optional.ofNullable(equipmentCache.get(id));
        if (equipment.isPresent()) {
            return equipment;
        }

        Optional<Equipment> equipmentFromService = loadEquipmentFromServiceById(id);
        equipmentFromService.ifPresent(e -> equipmentCache.put(e.id(), e));

        return equipmentFromService;
    }

    @Override
    public boolean loadDataPage(int page, int size) {
        Pair<List<Equipment>, Long> data = loadDataPageFromService(page, size);
        data.getLeft().forEach(equipment -> equipmentCache.put(equipment.id(), equipment));
        log.info("Loaded page {}. Count: {}, total count: {}", page, data.getLeft().size(), data.getRight());
        return (long) page * size + data.getLeft().size() < data.getRight();
    }

    @Override
    public boolean supports(EquipmentType equipmentType) {
        return this.equipmentType.equals(equipmentType);
    }

    protected abstract Pair<List<Equipment>, Long> loadDataPageFromService(int page, int size);

    protected abstract Optional<Equipment> loadEquipmentFromServiceById(int id);
}
