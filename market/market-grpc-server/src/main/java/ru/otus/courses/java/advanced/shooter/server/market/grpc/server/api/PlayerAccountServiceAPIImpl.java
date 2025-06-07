package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.PlayerAccountService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

@GRpcService
@RequiredArgsConstructor
public class PlayerAccountServiceAPIImpl extends PlayerAccountServiceAPIGrpc.PlayerAccountServiceAPIImplBase {
    private final PlayerAccountService playerAccountService;

    @Override
    public void getPlayerAccount(GetPlayerAccountRequest request, StreamObserver<PlayerAccountItemsPage> responseObserver) {
        responseObserver.onNext(playerAccountService.getPlayerAccount(request));
        responseObserver.onCompleted();
    }

    @Override
    public void initializePlayerAccount(InitializePlayerAccountRequest request, StreamObserver<Empty> responseObserver) {
        playerAccountService.initializePlayerAccount(request);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void giveCurrency(PlayerCurrencyOperationRequest request, StreamObserver<PlayerAccountItemInfo> responseObserver) {
        responseObserver.onNext(playerAccountService.giveCurrency(request));
        responseObserver.onCompleted();
    }

    @Override
    public void takeAwayCurrency(PlayerCurrencyOperationRequest request, StreamObserver<PlayerAccountItemInfo> responseObserver) {
        responseObserver.onNext(playerAccountService.takeAwayCurrency(request));
        responseObserver.onCompleted();
    }
}
