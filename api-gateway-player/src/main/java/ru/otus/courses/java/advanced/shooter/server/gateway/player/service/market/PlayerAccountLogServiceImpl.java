package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.GetPlayerAccountLogRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogServiceAPIGrpc;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerAccountLogServiceImpl implements PlayerAccountLogService {

    private final ObjectFactory<PlayerAccountLogServiceAPIGrpc.PlayerAccountLogServiceAPIBlockingStub>
            playerAccountLogServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<PlayerAccountLogPage> getPlayerAccountLogPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerAccountLogRequest request = GetPlayerAccountLogRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() ->
                        playerAccountLogServiceAPIBlockingStubObjectFactory.getObject().getPlayerAccountLog(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getPlayerAccountLog start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerAccountLog success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerAccountLog error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде