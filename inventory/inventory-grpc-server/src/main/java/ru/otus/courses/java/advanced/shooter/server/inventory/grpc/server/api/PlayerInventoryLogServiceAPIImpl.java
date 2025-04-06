package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryLogService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogServiceAPIGrpc;

@GRpcService
@RequiredArgsConstructor
public class PlayerInventoryLogServiceAPIImpl extends PlayerInventoryLogServiceAPIGrpc.PlayerInventoryLogServiceAPIImplBase {
    private final PlayerInventoryLogService playerInventoryLogService;

    @Override
    public void getPlayerInventoryLog(GetPlayerInventoryLogRequest request, StreamObserver<PlayerInventoryLogPage> responseObserver) {
        responseObserver.onNext(playerInventoryLogService.getPlayerInventoryLogPage(request));
        responseObserver.onCompleted();
    }
}
