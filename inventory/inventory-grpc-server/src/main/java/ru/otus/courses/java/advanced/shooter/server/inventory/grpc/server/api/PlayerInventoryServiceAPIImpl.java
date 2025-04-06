package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.*;

@GRpcService
@RequiredArgsConstructor
public class PlayerInventoryServiceAPIImpl extends PlayerInventoryServiceAPIGrpc.PlayerInventoryServiceAPIImplBase {
    private final PlayerInventoryService playerInventoryService;

    @Override
    public void getPlayerInventory(GetPlayerInventoryRequest request, StreamObserver<PlayerInventoryItemsPage> responseObserver) {
        responseObserver.onNext(playerInventoryService.getPlayerInventoryItemsPage(request));
        responseObserver.onCompleted();
    }

    @Override
    public void initializePlayerInventory(InitializePlayerInventoryRequest request, StreamObserver<Empty> responseObserver) {
        playerInventoryService.initializePlayerInventory(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void giveEquipment(PlayerEquipmentOperationRequest request, StreamObserver<Empty> responseObserver) {
        playerInventoryService.giveEquipment(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void takeAwayEquipment(PlayerEquipmentOperationRequest request, StreamObserver<Empty> responseObserver) {
        playerInventoryService.takeAwayEquipment(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void spendEquipment(PlayerEquipmentOperationRequest request, StreamObserver<Empty> responseObserver) {
        playerInventoryService.spendEquipment(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
