package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory;

import com.google.protobuf.Empty;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.equipment.contract.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemInfo;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

public interface InitialInventoryService {

    Mono<InitialPlayerInventoryItemsPage> getInitialInventoryItemsPage(GetInitialPlayerInventoryRequest.Filter filter, PaginationRequest paginationRequest);

    Mono<Empty> updateInitialInventory(UpdateInitialPlayerInventoryRequest request);
}
