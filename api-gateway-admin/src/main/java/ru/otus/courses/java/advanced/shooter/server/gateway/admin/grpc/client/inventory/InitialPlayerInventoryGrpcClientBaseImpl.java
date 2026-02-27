package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

@Slf4j
@Component(InitialPlayerInventoryGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class InitialPlayerInventoryGrpcClientBaseImpl implements InitialPlayerInventoryGrpcClient {

    public static final String NAME = "initialPlayerInventoryGrpcClientBaseImpl";

    private final ObjectFactory<InitialPlayerInventoryServiceAPIGrpc.InitialPlayerInventoryServiceAPIBlockingStub>
            initialPlayerInventoryServiceAPIBlockingStubObjectFactory;

    @Override
    public InitialPlayerInventoryItemsPage getInitialPlayerInventory(GetInitialPlayerInventoryRequest request) {
        log.debug("Getting initial player inventory with request: {}", request);
        return initialPlayerInventoryServiceAPIBlockingStubObjectFactory.getObject().getInitialPlayerInventory(request);
    }

    @Override
    public Empty updateInitialPlayerInventory(UpdateInitialPlayerInventoryRequest request) {
        log.debug("Updating initial player inventory with request: {}", request);
        return initialPlayerInventoryServiceAPIBlockingStubObjectFactory.getObject().updateInitialPlayerInventory(request);
    }
}
