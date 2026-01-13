package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryLogEntry;

import java.util.UUID;

@Repository
public interface PlayerInventoryLogRepository extends JpaRepository<PlayerInventoryLogEntry, Integer> {
    Page<PlayerInventoryLogEntry> findAllByPlayerUuid(UUID playerUuid, Pageable pageable);
}
