package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.inventory;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

import java.util.UUID;

public interface PlayerInventoryService {

    Mono<PlayerInventoryItemsPage> getPlayerInventoryItemsPage(String playerUuid, PaginationRequest paginationRequest);
}
