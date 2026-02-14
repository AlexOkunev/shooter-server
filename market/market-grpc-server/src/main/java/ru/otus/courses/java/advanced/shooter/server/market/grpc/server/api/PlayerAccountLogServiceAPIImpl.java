package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountLogEntry;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.PlayerAccountLogEntryProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountLogService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.GetPlayerAccountLogRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogServiceAPIGrpc;

import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class PlayerAccountLogServiceAPIImpl extends PlayerAccountLogServiceAPIGrpc.PlayerAccountLogServiceAPIImplBase {

    private final PlayerAccountLogService playerAccountLogService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final PlayerAccountLogEntryProtoMapper playerAccountLogEntryProtoMapper;

    @Override
    public void getPlayerAccountLog(GetPlayerAccountLogRequest request, StreamObserver<PlayerAccountLogPage> responseObserver) {
        UUID playerUuid = UUID.fromString(request.getPlayerUuid());
        Pageable pageable = playerAccountLogEntryProtoMapper.toPageable(request);

        Page<PlayerAccountLogEntry> data = playerAccountLogService.getPlayerAccountLogPage(playerUuid, pageable);

        PlayerAccountLogPage response = PlayerAccountLogPage.newBuilder()
                .addAllData(playerAccountLogEntryProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
