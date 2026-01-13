package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerEquipmentOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.bean.PlayerInventoryItemFilterParams;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.proto.PlayerInventoryProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.*;

import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class PlayerInventoryServiceAPIImpl extends PlayerInventoryServiceAPIGrpc.PlayerInventoryServiceAPIImplBase {

    private final PlayerInventoryService playerInventoryService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final PlayerInventoryProtoMapper playerInventoryProtoMapper;

    @Override
    public void getPlayerInventory(GetPlayerInventoryRequest request, StreamObserver<PlayerInventoryItemsPage> responseObserver) {
        UUID playerUuid = UUID.fromString(request.getPlayerUuid());
        Pageable pageRequest = playerInventoryProtoMapper.toPageable(request);
        PlayerInventoryItemFilterParams filterParams = playerInventoryProtoMapper.toFilterParams(request);

        Page<PlayerInventoryItem> data = playerInventoryService.getPlayerInventoryItemsPage(playerUuid, pageRequest, filterParams);

        PlayerInventoryItemsPage response = PlayerInventoryItemsPage.newBuilder()
                .addAllData(playerInventoryProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void initializePlayerInventory(InitializePlayerInventoryRequest request, StreamObserver<Empty> responseObserver) {
        UUID playerUuid = UUID.fromString(request.getPlayerUuid());
        playerInventoryService.initializePlayerInventory(playerUuid);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void giveEquipment(PlayerEquipmentOperationRequest request, StreamObserver<Empty> responseObserver) {
        PlayerEquipmentOperationCommand command = playerInventoryProtoMapper.toCommand(request);
        playerInventoryService.giveEquipment(command);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void takeAwayEquipment(PlayerEquipmentOperationRequest request, StreamObserver<Empty> responseObserver) {
        PlayerEquipmentOperationCommand command = playerInventoryProtoMapper.toCommand(request);
        playerInventoryService.takeAwayEquipment(command);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void spendEquipment(PlayerEquipmentOperationRequest request, StreamObserver<Empty> responseObserver) {
        PlayerEquipmentOperationCommand command = playerInventoryProtoMapper.toCommand(request);
        playerInventoryService.spendEquipment(command);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
