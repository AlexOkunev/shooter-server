package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

@Slf4j
@RequiredArgsConstructor
@Component(PlayerAccountGrpcClientBaseImpl.NAME)
public class PlayerAccountGrpcClientBaseImpl implements PlayerAccountGrpcClient {

    public static final String NAME = "playerAccountGrpcClientBaseImpl";

    private final ObjectFactory<PlayerAccountServiceAPIGrpc.PlayerAccountServiceAPIBlockingStub>
            playerAccountServiceAPIBlockingStubObjectFactory;

    @Override
    public PlayerAccountItemsPage getPlayerAccount(GetPlayerAccountRequest request) {
        log.debug("Getting player account with request: {}", request);
        return playerAccountServiceAPIBlockingStubObjectFactory.getObject().getPlayerAccount(request);
    }

    @Override
    public Empty initializePlayerAccount(InitializePlayerAccountRequest request) {
        log.debug("Initializing player account with request: {}", request);
        return playerAccountServiceAPIBlockingStubObjectFactory.getObject().initializePlayerAccount(request);
    }

    @Override
    public PlayerAccountItemInfo giveCurrency(PlayerCurrencyOperationRequest request) {
        log.debug("Giving currency to player with request: {}", request);
        return playerAccountServiceAPIBlockingStubObjectFactory.getObject().giveCurrency(request);
    }

    @Override
    public PlayerAccountItemInfo takeAwayCurrency(PlayerCurrencyOperationRequest request) {
        log.debug("Taking away currency from player with request: {}", request);
        return playerAccountServiceAPIBlockingStubObjectFactory.getObject().takeAwayCurrency(request);
    }
}
