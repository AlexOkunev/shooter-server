package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryLogEntry;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.mapper.proto.PlayerInventoryLogEntryProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.service.PlayerInventoryLogService;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogServiceAPIGrpc;

import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class PlayerInventoryLogServiceAPIImpl extends PlayerInventoryLogServiceAPIGrpc.PlayerInventoryLogServiceAPIImplBase {
    private final PlayerInventoryLogService playerInventoryLogService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final PlayerInventoryLogEntryProtoMapper playerInventoryLogEntryProtoMapper;

    @Override
    public void getPlayerInventoryLog(GetPlayerInventoryLogRequest request, StreamObserver<PlayerInventoryLogPage> responseObserver) {
        UUID playerUuid = UUID.fromString(request.getPlayerUuid());
        Pageable pageable = playerInventoryLogEntryProtoMapper.toPageable(request);

        Page<PlayerInventoryLogEntry> data = playerInventoryLogService.getPlayerInventoryLogPage(playerUuid, pageable);

        PlayerInventoryLogPage response = PlayerInventoryLogPage.newBuilder()
                .addAllData(playerInventoryLogEntryProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
