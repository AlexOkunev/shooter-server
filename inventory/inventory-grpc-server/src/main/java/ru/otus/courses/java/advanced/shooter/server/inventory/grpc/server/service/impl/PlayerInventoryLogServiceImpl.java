package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryLogEntry;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository.PlayerInventoryLogRepository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryLogService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class PlayerInventoryLogServiceImpl implements PlayerInventoryLogService {
    private final PlayerInventoryLogRepository playerInventoryLogRepository;

    @Override
    public Page<PlayerInventoryLogEntry> getPlayerInventoryLogPage(@NotNull UUID playerUuid, @NotNull Pageable pageable) {
        return playerInventoryLogRepository.findAllByPlayerUuid(playerUuid, pageable);
    }
}
