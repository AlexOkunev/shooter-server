package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.ModifyInitialPlayerInventoryRequest;

public interface InitialPlayerInventoryService {
    InitialPlayerInventoryItemsPage getInitialPlayerInventory(GetInitialPlayerInventoryRequest request);

    void modifyInitialPlayerInventory(ModifyInitialPlayerInventoryRequest request);
}
