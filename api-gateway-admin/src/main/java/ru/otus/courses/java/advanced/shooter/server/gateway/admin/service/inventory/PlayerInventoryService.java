package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory;

import com.google.protobuf.Empty;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;

public interface PlayerInventoryService {

    Mono<PlayerInventoryItemsPage> getPlayerInventoryItemsPage(String playerUuid, PaginationRequest paginationRequest);

    Mono<Empty> giveEquipment(String playerUuid, EquipmentType equipmentType, int equipmentId, int amount);

    Mono<Empty> takeAwayEquipment(String playerUuid, EquipmentType equipmentType, int equipmentId, int amount);

    Mono<Empty> initializeInventory(String playerUuid);
}
