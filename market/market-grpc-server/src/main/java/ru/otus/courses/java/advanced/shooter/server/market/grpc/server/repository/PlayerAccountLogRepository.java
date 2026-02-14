package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountLogEntry;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountLogEntryId;

import java.util.UUID;

@Repository
public interface PlayerAccountLogRepository extends JpaRepository<PlayerAccountLogEntry, PlayerAccountLogEntryId> {
    Page<PlayerAccountLogEntry> findAllByPlayerUuid(UUID playerUuid, Pageable pageable);
}
