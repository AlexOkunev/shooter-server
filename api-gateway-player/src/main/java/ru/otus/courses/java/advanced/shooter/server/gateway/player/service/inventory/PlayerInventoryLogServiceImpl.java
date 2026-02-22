package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.GetPlayerInventoryLogRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.log.PlayerInventoryLogServiceAPIGrpc;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInventoryLogServiceImpl implements PlayerInventoryLogService {

    private final ObjectFactory<PlayerInventoryLogServiceAPIGrpc.PlayerInventoryLogServiceAPIBlockingStub>
            playerInventoryLogServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<PlayerInventoryLogPage> getPlayerInventoryLogPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerInventoryLogRequest request = GetPlayerInventoryLogRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() ->
                        playerInventoryLogServiceAPIBlockingStubObjectFactory.getObject().getPlayerInventoryLog(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getPlayerInventoryLog start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerInventoryLog success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerInventoryLog error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде