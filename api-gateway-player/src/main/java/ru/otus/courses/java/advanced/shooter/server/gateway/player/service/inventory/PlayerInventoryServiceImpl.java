package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.GetPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.PlayerInventoryServiceAPIGrpc;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlayerInventoryServiceImpl implements PlayerInventoryService {

    private final ObjectFactory<PlayerInventoryServiceAPIGrpc.PlayerInventoryServiceAPIBlockingStub>
            playerInventoryServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<PlayerInventoryItemsPage> getPlayerInventoryItemsPage(String playerUuid, PaginationRequest paginationRequest) {
        GetPlayerInventoryRequest request = GetPlayerInventoryRequest.newBuilder()
                .setPlayerUuid(playerUuid)
                .setFilter(GetPlayerInventoryRequest.Filter.newBuilder()
                        .setEnabled(true)
                        .build()
                )
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() ->
                        playerInventoryServiceAPIBlockingStubObjectFactory.getObject().getPlayerInventory(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getPlayerInventory start playerUuid={}", playerUuid))
                .doOnSuccess(resp -> log.info("gRPC getPlayerInventory success playerUuid={}", playerUuid))
                .doOnError(e -> log.error("gRPC getPlayerInventory error playerUuid={} err={}", playerUuid, e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде