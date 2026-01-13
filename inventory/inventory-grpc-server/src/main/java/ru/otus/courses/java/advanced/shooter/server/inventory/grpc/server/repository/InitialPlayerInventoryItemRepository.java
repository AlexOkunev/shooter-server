package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;

import java.util.List;

@Repository
public interface InitialPlayerInventoryItemRepository extends JpaRepository<InitialPlayerInventoryItem, ReferenceEquipmentId>, JpaSpecificationExecutor<InitialPlayerInventoryItem> {
    List<InitialPlayerInventoryItem> findAllByEnabledIsTrue();
}
