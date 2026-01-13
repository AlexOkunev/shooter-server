package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItemId;

import java.util.Optional;
import java.util.UUID;

public interface PlayerInventoryItemRepository extends JpaRepository<PlayerInventoryItem, PlayerInventoryItemId>, JpaSpecificationExecutor<PlayerInventoryItem> {

    Optional<PlayerInventoryItem> findFirstByIdPlayerUuid(UUID playerUuid);

    boolean existsByIdPlayerUuid(UUID playerUuid);
}
