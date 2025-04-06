package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

public interface PlayerInventoryLogService {
    PlayerInventoryLogPage getPlayerInventoryLogPage(GetPlayerInventoryLogRequest request);
}
