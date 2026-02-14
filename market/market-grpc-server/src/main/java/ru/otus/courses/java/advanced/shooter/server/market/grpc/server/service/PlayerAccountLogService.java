package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountLogEntry;

import java.util.UUID;

public interface PlayerAccountLogService {
    Page<PlayerAccountLogEntry> getPlayerAccountLogPage(@NotNull UUID playerUuid, @NotNull Pageable pageable);
}
