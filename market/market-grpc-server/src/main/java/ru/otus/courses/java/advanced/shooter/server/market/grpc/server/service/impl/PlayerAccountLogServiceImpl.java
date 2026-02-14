package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountLogEntry;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository.PlayerAccountLogRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountLogService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class PlayerAccountLogServiceImpl implements PlayerAccountLogService {
    private final PlayerAccountLogRepository playerAccountLogRepository;

    @Override
    public Page<PlayerAccountLogEntry> getPlayerAccountLogPage(@NotNull UUID playerUuid, @NotNull Pageable pageable) {
        return playerAccountLogRepository.findAllByPlayerUuid(playerUuid, pageable);
    }
}
