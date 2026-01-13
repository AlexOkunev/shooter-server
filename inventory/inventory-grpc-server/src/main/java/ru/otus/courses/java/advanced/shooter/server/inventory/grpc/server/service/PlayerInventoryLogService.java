package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryLogEntry;

import java.util.UUID;

public interface PlayerInventoryLogService {
    Page<PlayerInventoryLogEntry> getPlayerInventoryLogPage(@NotNull UUID playerUuid, @NotNull Pageable pageable);
}
