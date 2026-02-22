package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialInventoryServiceImpl implements InitialInventoryService {

    private final ObjectFactory<InitialPlayerInventoryServiceAPIGrpc.InitialPlayerInventoryServiceAPIBlockingStub>
            initialPlayerInventoryServiceAPIBlockingStubObjectFactory;

    @Override
    public Mono<InitialPlayerInventoryItemsPage> getInitialInventoryItemsPage(GetInitialPlayerInventoryRequest.Filter filter, PaginationRequest paginationRequest) {
        GetInitialPlayerInventoryRequest request = GetInitialPlayerInventoryRequest.newBuilder()
                .setFilter(filter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() ->
                        initialPlayerInventoryServiceAPIBlockingStubObjectFactory.getObject().getInitialPlayerInventory(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getInitialPlayerInventory start"))
                .doOnSuccess(resp -> log.info("gRPC getInitialPlayerInventory success"))
                .doOnError(e -> log.error("gRPC getInitialPlayerInventory error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<Empty> updateInitialInventory(UpdateInitialPlayerInventoryRequest request) {
        return Mono.fromCallable(() ->
                        initialPlayerInventoryServiceAPIBlockingStubObjectFactory.getObject().updateInitialPlayerInventory(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC updateInitialPlayerInventory start"))
                .doOnSuccess(resp -> log.info("gRPC updateInitialPlayerInventory success"))
                .doOnError(e -> log.error("gRPC updateInitialPlayerInventory error err={}", e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде