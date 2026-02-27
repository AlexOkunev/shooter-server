package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.*;

@Slf4j
@Component(PlayerGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class PlayerGrpcClientBaseImpl implements PlayerGrpcClient {

    public static final String NAME = "playerGrpcClientBaseImpl";

    private final ObjectFactory<ShooterPlayersServiceAPIGrpc.ShooterPlayersServiceAPIBlockingStub>
            playersServiceAPIBlockingStubObjectFactory;

    @Override
    public PlayerInfo getPlayer(GetPlayerRequest request) {
        log.debug("Getting player with request: {}", request);
        return playersServiceAPIBlockingStubObjectFactory.getObject().getPlayer(request);
    }

    @Override
    public PlayerInfoListPage getPlayers(GetPlayersRequest request) {
        log.debug("Getting players with request: {}", request);
        return playersServiceAPIBlockingStubObjectFactory.getObject().getPlayers(request);
    }
}
