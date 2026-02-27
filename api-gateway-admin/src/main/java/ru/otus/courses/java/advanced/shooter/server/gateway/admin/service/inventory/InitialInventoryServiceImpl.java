package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.inventory;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory.InitialPlayerInventoryGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.inventory.InitialPlayerInventoryGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.GetInitialPlayerInventoryRequest;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.InitialPlayerInventoryItemsPage;
import ru.otus.courses.java.advanced.shooter.server.inventory.protobuf.inventory.initial.UpdateInitialPlayerInventoryRequest;

@Slf4j
@Service
public class InitialInventoryServiceImpl implements InitialInventoryService {

    private final InitialPlayerInventoryGrpcClient initialPlayerInventoryGrpcClient;
    private final Scheduler schedulerInventory;

    public InitialInventoryServiceImpl(
            @Qualifier(InitialPlayerInventoryGrpcClientRateLimitingWrapper.NAME) InitialPlayerInventoryGrpcClient initialPlayerInventoryGrpcClient,
            @Qualifier(GrpcSchedulers.INVENTORY) Scheduler schedulerInventory
    ) {
        this.initialPlayerInventoryGrpcClient = initialPlayerInventoryGrpcClient;
        this.schedulerInventory = schedulerInventory;
    }

    @Override
    public Mono<InitialPlayerInventoryItemsPage> getInitialInventoryItemsPage(GetInitialPlayerInventoryRequest.Filter filter, PaginationRequest paginationRequest) {
        GetInitialPlayerInventoryRequest request = GetInitialPlayerInventoryRequest.newBuilder()
                .setFilter(filter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> initialPlayerInventoryGrpcClient.getInitialPlayerInventory(request))
                .subscribeOn(schedulerInventory)
                .doOnSubscribe(s -> log.info("gRPC getInitialPlayerInventory start"))
                .doOnSuccess(resp -> log.info("gRPC getInitialPlayerInventory success"))
                .doOnError(e -> log.error("gRPC getInitialPlayerInventory error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<Empty> updateInitialInventory(UpdateInitialPlayerInventoryRequest request) {
        return Mono.fromCallable(() -> initialPlayerInventoryGrpcClient.updateInitialPlayerInventory(request))
                .subscribeOn(schedulerInventory)
                .doOnSubscribe(s -> log.info("gRPC updateInitialPlayerInventory start"))
                .doOnSuccess(resp -> log.info("gRPC updateInitialPlayerInventory success"))
                .doOnError(e -> log.error("gRPC updateInitialPlayerInventory error err={}", e.getMessage(), e));
    }
}