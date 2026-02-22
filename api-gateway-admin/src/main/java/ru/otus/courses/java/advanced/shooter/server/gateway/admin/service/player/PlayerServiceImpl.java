package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final ObjectFactory<ShooterPlayersServiceAPIGrpc.ShooterPlayersServiceAPIBlockingStub> playerServiceStubObjectFactory;

    @Override
    public Mono<PlayerInfo> getPlayerInfo(String uuid) {
        GetPlayerRequest request = GetPlayerRequest.newBuilder()
                .setPlayerUuid(uuid)
                .build();

        return Mono.fromCallable(() -> playerServiceStubObjectFactory.getObject().getPlayer(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getPlayer start uuid={}", uuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayer success uuid={}", uuid))
                .doOnError(e -> log.error("gRPC getPlayer error uuid={} err={}",
                        uuid, e.getMessage(), e));
    }

    @Override
    public Mono<PlayerInfoListPage> searchPlayers(PlayersFilter requestFilter, PaginationRequest paginationRequest) {
        GetPlayersRequest request = GetPlayersRequest.newBuilder()
                .setFilter(requestFilter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> playerServiceStubObjectFactory.getObject().getPlayers(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getPlayers start"))
                .doOnSuccess(resp -> log.info("gRPC getPlayers success items={}", resp != null ? resp.getDataCount() : 0))
                .doOnError(e -> log.error("gRPC getPlayers error: {}", e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности