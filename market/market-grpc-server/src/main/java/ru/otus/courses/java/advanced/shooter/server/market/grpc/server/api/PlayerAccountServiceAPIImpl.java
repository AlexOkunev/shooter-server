package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.PlayerCurrencyOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.PlayerAccountItemProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class PlayerAccountServiceAPIImpl extends PlayerAccountServiceAPIGrpc.PlayerAccountServiceAPIImplBase {
    private final PlayerAccountService playerAccountService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final PlayerAccountItemProtoMapper playerAccountItemProtoMapper;

    @Override
    public void getPlayerAccount(GetPlayerAccountRequest request, StreamObserver<PlayerAccountItemsPage> responseObserver) {
        Pageable pageable = playerAccountItemProtoMapper.toPageable(request);
        UUID playerUuid = UUID.fromString(request.getPlayerUuid());
        boolean onlyEnabledCurrencies = request.hasOnlyEnabledCurrencies() && request.getOnlyEnabledCurrencies();

        Page<PlayerAccountItem> data = playerAccountService.getPlayerAccountItems(
                playerUuid,
                onlyEnabledCurrencies,
                pageable
        );

        PlayerAccountItemsPage response = PlayerAccountItemsPage.newBuilder()
                .addAllData(playerAccountItemProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void initializePlayerAccount(InitializePlayerAccountRequest request, StreamObserver<Empty> responseObserver) {
        UUID playerUuid = UUID.fromString(request.getPlayerUuid());
        playerAccountService.initializePlayerAccount(playerUuid);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void giveCurrency(PlayerCurrencyOperationRequest request, StreamObserver<PlayerAccountItemInfo> responseObserver) {
        PlayerCurrencyOperationCommand command = playerAccountItemProtoMapper.toCommand(request);
        PlayerAccountItem item = playerAccountService.giveCurrency(command);
        PlayerAccountItemInfo response = playerAccountItemProtoMapper.toResponse(item);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void takeAwayCurrency(PlayerCurrencyOperationRequest request, StreamObserver<PlayerAccountItemInfo> responseObserver) {
        PlayerCurrencyOperationCommand command = playerAccountItemProtoMapper.toCommand(request);
        PlayerAccountItem item = playerAccountService.takeAwayCurrency(command);
        PlayerAccountItemInfo response = playerAccountItemProtoMapper.toResponse(item);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}