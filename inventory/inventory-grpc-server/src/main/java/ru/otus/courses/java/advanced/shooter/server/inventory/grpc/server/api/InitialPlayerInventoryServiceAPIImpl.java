package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.InitialPlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.ModifyInitialPlayerInventoryRequest;

@GRpcService
@RequiredArgsConstructor
public class InitialPlayerInventoryServiceAPIImpl extends InitialPlayerInventoryServiceAPIGrpc.InitialPlayerInventoryServiceAPIImplBase {
    private final InitialPlayerInventoryService initialPlayerInventoryService;

    @Override
    public void getInitialPlayerInventory(GetInitialPlayerInventoryRequest request, StreamObserver<InitialPlayerInventoryItemsPage> responseObserver) {
        responseObserver.onNext(initialPlayerInventoryService.getInitialPlayerInventory(request));
        responseObserver.onCompleted();
    }

    @Override
    public void modifyInitialPlayerInventory(ModifyInitialPlayerInventoryRequest request, StreamObserver<Empty> responseObserver) {
        initialPlayerInventoryService.modifyInitialPlayerInventory(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
