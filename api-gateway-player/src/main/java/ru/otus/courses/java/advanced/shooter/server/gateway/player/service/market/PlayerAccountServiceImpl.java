package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.GetPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountServiceAPIGrpc;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerAccountServiceImpl implements PlayerAccountService {

    private final ObjectFactory<PlayerAccountServiceAPIGrpc.PlayerAccountServiceAPIBlockingStub>
            playerAccountServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<PlayerAccountItemsPage> getPlayerAccountItemsPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerAccountRequest request = GetPlayerAccountRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .setOnlyEnabledCurrencies(true)
                .build();

        return Mono.fromCallable(() ->
                        playerAccountServiceAPIBlockingStubObjectFactory.getObject().getPlayerAccount(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getPlayerAccount start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerAccount success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerAccount error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде