package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.InitialPlayerInventoryFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.UpdateInitialPlayerInventoryCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.proto.InitialPlayerInventoryProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.InitialPlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

@GRpcService
@RequiredArgsConstructor
public class InitialPlayerInventoryServiceAPIImpl extends InitialPlayerInventoryServiceAPIGrpc.InitialPlayerInventoryServiceAPIImplBase {
    private final InitialPlayerInventoryService initialPlayerInventoryService;
    private final InitialPlayerInventoryProtoMapper initialPlayerInventoryProtoMapper;
    private final PaginationInfoMapper paginationInfoMapper;

    @Override
    public void getInitialPlayerInventory(GetInitialPlayerInventoryRequest request, StreamObserver<InitialPlayerInventoryItemsPage> responseObserver) {
        InitialPlayerInventoryFilterParams filterParams = initialPlayerInventoryProtoMapper.toFilterParams(request);
        Pageable pageable = initialPlayerInventoryProtoMapper.toPageable(request);

        Page<InitialPlayerInventoryItem> data = initialPlayerInventoryService.getInitialPlayerInventory(filterParams, pageable);

        InitialPlayerInventoryItemsPage response = InitialPlayerInventoryItemsPage.newBuilder()
                .addAllData(initialPlayerInventoryProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateInitialPlayerInventory(UpdateInitialPlayerInventoryRequest request, StreamObserver<Empty> responseObserver) {
        UpdateInitialPlayerInventoryCommand command = initialPlayerInventoryProtoMapper.toCommand(request);
        initialPlayerInventoryService.updateInitialPlayerInventory(command);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
