package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service.PlayerService;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerResponse;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.ShooterPlayersServicePublicAPIGrpc;

@GRpcService
@RequiredArgsConstructor
public class ShooterPlayersServicePublicAPIImpl extends ShooterPlayersServicePublicAPIGrpc.ShooterPlayersServicePublicAPIImplBase {
    private final PlayerService playerService;

    @Override
    public void getPlayer(GetPlayerRequest request, StreamObserver<GetPlayerResponse> responseObserver) {
        responseObserver.onNext(playerService.getPlayer(request));
        responseObserver.onCompleted();
    }
}
