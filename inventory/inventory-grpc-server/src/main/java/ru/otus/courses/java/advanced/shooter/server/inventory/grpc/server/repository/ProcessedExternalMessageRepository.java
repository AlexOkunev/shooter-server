package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ProcessedExternalMessage;

import java.util.UUID;

@Repository
public interface ProcessedExternalMessageRepository extends JpaRepository<ProcessedExternalMessage, UUID> {
    boolean existsByUuid(UUID uuid);
}
