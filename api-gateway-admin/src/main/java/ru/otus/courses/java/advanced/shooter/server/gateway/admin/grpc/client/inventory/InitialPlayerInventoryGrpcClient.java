package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory;

import com.google.protobuf.Empty;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

public interface InitialPlayerInventoryGrpcClient {

    InitialPlayerInventoryItemsPage getInitialPlayerInventory(GetInitialPlayerInventoryRequest request);

    Empty updateInitialPlayerInventory(UpdateInitialPlayerInventoryRequest request);
}
