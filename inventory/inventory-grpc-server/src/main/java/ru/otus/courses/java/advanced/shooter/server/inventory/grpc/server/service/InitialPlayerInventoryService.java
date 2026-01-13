package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.InitialPlayerInventoryFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.UpdateInitialPlayerInventoryCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;

public interface InitialPlayerInventoryService {

    Page<InitialPlayerInventoryItem> getInitialPlayerInventory(
            @NotNull InitialPlayerInventoryFilterParams filterParams,
            @NotNull Pageable pageable
    );

    void updateInitialPlayerInventory(@Valid @NotNull UpdateInitialPlayerInventoryCommand command);
}
