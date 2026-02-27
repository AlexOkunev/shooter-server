package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory;

import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

public interface PlayerInventoryLogGrpcClient {

    PlayerInventoryLogPage getPlayerInventoryLog(GetPlayerInventoryLogRequest request);
}
