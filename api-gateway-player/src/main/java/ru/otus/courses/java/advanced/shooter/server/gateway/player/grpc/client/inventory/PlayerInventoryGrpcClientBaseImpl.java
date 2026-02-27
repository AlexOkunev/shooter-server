package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.inventory;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.*;

@Slf4j
@Component(PlayerInventoryGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class PlayerInventoryGrpcClientBaseImpl implements PlayerInventoryGrpcClient {

    public static final String NAME = "playerInventoryGrpcClientBaseImpl";

    private final ObjectFactory<PlayerInventoryServiceAPIGrpc.PlayerInventoryServiceAPIBlockingStub>
            playerInventoryServiceAPIBlockingStubObjectFactory;

    @Override
    public PlayerInventoryItemsPage getPlayerInventory(GetPlayerInventoryRequest request) {
        log.debug("Getting player inventory with request: {}", request);
        return playerInventoryServiceAPIBlockingStubObjectFactory.getObject().getPlayerInventory(request);
    }

    @Override
    public Empty initializePlayerInventory(InitializePlayerInventoryRequest request) {
        log.debug("Initializing player inventory with request: {}", request);
        return playerInventoryServiceAPIBlockingStubObjectFactory.getObject().initializePlayerInventory(request);
    }

    @Override
    public Empty giveEquipment(PlayerEquipmentOperationRequest request) {
        log.debug("Giving equipment to player with request: {}", request);
        return playerInventoryServiceAPIBlockingStubObjectFactory.getObject().giveEquipment(request);
    }

    @Override
    public Empty takeAwayEquipment(PlayerEquipmentOperationRequest request) {
        log.debug("Taking away equipment from player with request: {}", request);
        return playerInventoryServiceAPIBlockingStubObjectFactory.getObject().takeAwayEquipment(request);
    }

    @Override
    public Empty spendEquipment(PlayerEquipmentOperationRequest request) {
        log.debug("Spending equipment from player with request: {}", request);
        return playerInventoryServiceAPIBlockingStubObjectFactory.getObject().spendEquipment(request);
    }
}
