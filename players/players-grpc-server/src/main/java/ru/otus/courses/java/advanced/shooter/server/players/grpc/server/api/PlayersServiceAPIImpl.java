package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.api;


import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.mapper.PlayerMapper;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service.PlayerService;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.*;

import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class PlayersServiceAPIImpl extends ShooterPlayersServiceAPIGrpc.ShooterPlayersServiceAPIImplBase {
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final PaginationInfoMapper paginationInfoMapper;

    @Override
    public void getPlayer(GetPlayerRequest request, StreamObserver<PlayerInfo> responseObserver) {
        Player player = switch (request.getFieldCase()) {
            case PLAYER_UUID -> playerService.getPlayerByPlayerUuid(UUID.fromString(request.getPlayerUuid()));
            case KEYCLOAK_ID -> playerService.getPlayerByKeycloakId(request.getKeycloakId());
            case FIELD_NOT_SET -> throw new IllegalArgumentException("Field case not set in request");
        };

        PlayerInfo response = playerMapper.toResponse(player);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPlayers(GetPlayersRequest request, StreamObserver<PlayerInfoListPage> responseObserver) {
        Page<Player> data = playerService.getPlayers(
                playerMapper.toFilterParams(request),
                playerMapper.toPageable(request)
        );

        PlayerInfoListPage response = PlayerInfoListPage.newBuilder()
                .addAllData(data.map(playerMapper::toResponse))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
