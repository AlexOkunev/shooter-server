package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProcessedExternalMessage;

import java.util.UUID;

@Repository
public interface ProcessedExternalMessageRepository extends JpaRepository<ProcessedExternalMessage, Integer> {
    boolean existsByMessageUUID(UUID uuid);
}
