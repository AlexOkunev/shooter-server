package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

@Slf4j
@Component(InitialPlayerAccountGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class InitialPlayerAccountGrpcClientBaseImpl implements InitialPlayerAccountGrpcClient {

    public static final String NAME = "initialPlayerAccountGrpcClientBaseImpl";

    private final ObjectFactory<InitialPlayerAccountServiceAPIGrpc.InitialPlayerAccountServiceAPIBlockingStub>
            initialPlayerAccountServiceAPIBlockingStubObjectFactory;


    @Override
    public InitialPlayerAccountItemsPage getInitialPlayerAccount(GetInitialPlayerAccountRequest request) {
        log.debug("Getting initial player account with request: {}", request);
        return initialPlayerAccountServiceAPIBlockingStubObjectFactory.getObject().getInitialPlayerAccount(request);
    }

    @Override
    public Empty updateInitialPlayerAccount(UpdateInitialPlayerAccountRequest request) {
        log.debug("Updating initial player account with request: {}", request);
        return initialPlayerAccountServiceAPIBlockingStubObjectFactory.getObject().updateInitialPlayerAccount(request);
    }
}
