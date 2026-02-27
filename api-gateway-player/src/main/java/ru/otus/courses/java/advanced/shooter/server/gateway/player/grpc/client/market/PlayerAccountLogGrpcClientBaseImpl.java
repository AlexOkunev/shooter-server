package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.GetPlayerAccountLogRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogServiceAPIGrpc;

@Slf4j
@Component(PlayerAccountLogGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class PlayerAccountLogGrpcClientBaseImpl implements PlayerAccountLogGrpcClient {

    public static final String NAME = "playerAccountLogGrpcClientBaseImpl";

    private final ObjectFactory<PlayerAccountLogServiceAPIGrpc.PlayerAccountLogServiceAPIBlockingStub>
            playerAccountLogServiceAPIBlockingStubObjectFactory;

    @Override
    public PlayerAccountLogPage getPlayerAccountLog(GetPlayerAccountLogRequest request) {
        log.debug("Getting player account log with request: {}", request);
        return playerAccountLogServiceAPIBlockingStubObjectFactory.getObject().getPlayerAccountLog(request);
    }
}
