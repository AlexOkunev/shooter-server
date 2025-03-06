package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service.PlayerService;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.*;

@GRpcService
@RequiredArgsConstructor
public class ShooterPlayersServicePrivateAPIImpl extends ShooterPlayersServicePrivateAPIGrpc.ShooterPlayersServicePrivateAPIImplBase {
    private final PlayerService playerService;

    @Override
    public void getPlayer(GetPlayerRequest request, StreamObserver<GetPlayerResponse> responseObserver) {
        responseObserver.onNext(playerService.getPlayer(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getPlayers(GetPlayersRequest request, StreamObserver<GetPlayersResponse> responseObserver) {
        responseObserver.onNext(playerService.getPlayers(request));
        responseObserver.onCompleted();
    }
}
