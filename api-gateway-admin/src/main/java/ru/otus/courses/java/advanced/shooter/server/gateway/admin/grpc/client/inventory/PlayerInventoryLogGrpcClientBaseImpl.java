package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogServiceAPIGrpc;

@Slf4j
@Component(PlayerInventoryLogGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class PlayerInventoryLogGrpcClientBaseImpl implements PlayerInventoryLogGrpcClient {

    public static final String NAME = "playerInventoryLogGrpcClientBaseImpl";

    private final ObjectFactory<PlayerInventoryLogServiceAPIGrpc.PlayerInventoryLogServiceAPIBlockingStub>
            playerInventoryLogServiceAPIBlockingStubObjectFactory;

    @Override
    public PlayerInventoryLogPage getPlayerInventoryLog(GetPlayerInventoryLogRequest request) {
        log.debug("Getting player inventory log with request: {}", request);
        return playerInventoryLogServiceAPIBlockingStubObjectFactory.getObject().getPlayerInventoryLog(request);
    }
}
