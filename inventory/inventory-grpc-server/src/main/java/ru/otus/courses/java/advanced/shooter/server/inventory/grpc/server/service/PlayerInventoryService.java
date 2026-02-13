package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerEquipmentOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerInventoryItemFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;

import java.util.UUID;

public interface PlayerInventoryService {
    void initializePlayerInventory(@NotNull UUID playerUuid);

    void initializePlayerInventoryBySystemEvent(@NotNull UUID playerUuid);

    void giveEquipment(@Valid @NotNull PlayerEquipmentOperationCommand command);

    boolean buyEquipment(@NotNull UUID tradeUuid, @Valid @NotNull PlayerEquipmentOperationCommand command);

    void takeAwayEquipment(@Valid @NotNull PlayerEquipmentOperationCommand command);

    void spendEquipment(@Valid @NotNull PlayerEquipmentOperationCommand command);

    Page<PlayerInventoryItem> getPlayerInventoryItemsPage(
            @NotNull UUID playerUuid,
            @NotNull Pageable pageRequest,
            @NotNull PlayerInventoryItemFilterParams filterParams
    );
}
