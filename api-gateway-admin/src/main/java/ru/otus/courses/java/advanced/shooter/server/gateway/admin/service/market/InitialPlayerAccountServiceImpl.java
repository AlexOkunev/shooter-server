package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import com.google.protobuf.Empty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.InitialPlayerAccountGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market.InitialPlayerAccountGrpcClientRateLimitingWrapper;
import ru.otus.courses.java.advanced.shooter.server.gateway.admin.util.GrpcSchedulers;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

@Slf4j
@Service
public class InitialPlayerAccountServiceImpl implements InitialPlayerAccountService {

    private final InitialPlayerAccountGrpcClient initialPlayerAccountGrpcClient;
    private final Scheduler schedulerMarket;

    public InitialPlayerAccountServiceImpl(
            @Qualifier(InitialPlayerAccountGrpcClientRateLimitingWrapper.NAME) InitialPlayerAccountGrpcClient initialPlayerAccountGrpcClient,
            @Qualifier(GrpcSchedulers.MARKET) Scheduler schedulerMarket
    ) {
        this.initialPlayerAccountGrpcClient = initialPlayerAccountGrpcClient;
        this.schedulerMarket = schedulerMarket;
    }

    @Override
    public Mono<InitialPlayerAccountItemsPage> getInitialAccountItemsPage(GetInitialPlayerAccountRequest.Filter filter, PaginationRequest paginationRequest) {
        GetInitialPlayerAccountRequest request = GetInitialPlayerAccountRequest.newBuilder()
                .setFilter(filter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() -> initialPlayerAccountGrpcClient.getInitialPlayerAccount(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC getInitialPlayerAccount start"))
                .doOnSuccess(resp -> log.info("gRPC getInitialPlayerAccount success"))
                .doOnError(e -> log.error("gRPC getInitialPlayerAccount error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<Empty> updateInitialAccount(UpdateInitialPlayerAccountRequest request) {
        return Mono.fromCallable(() -> initialPlayerAccountGrpcClient.updateInitialPlayerAccount(request))
                .subscribeOn(schedulerMarket)
                .doOnSubscribe(s -> log.info("gRPC updateInitialPlayerAccount start"))
                .doOnSuccess(resp -> log.info("gRPC updateInitialPlayerAccount success"))
                .doOnError(e -> log.error("gRPC updateInitialPlayerAccount error err={}", e.getMessage(), e));
    }
}