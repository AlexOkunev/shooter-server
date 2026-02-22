package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;

public interface PlayerInventoryLogService {

    Mono<PlayerInventoryLogPage> getPlayerInventoryLogPage(String playerUuid, PaginationRequest paginationRequest);
}
