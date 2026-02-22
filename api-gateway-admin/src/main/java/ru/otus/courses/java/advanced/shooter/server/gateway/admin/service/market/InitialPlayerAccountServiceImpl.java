package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountServiceAPIGrpc;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitialPlayerAccountServiceImpl implements InitialPlayerAccountService {

    private final ObjectFactory<InitialPlayerAccountServiceAPIGrpc.InitialPlayerAccountServiceAPIBlockingStub>
            initialPlayerAccountServiceAPIBlockingStubObjectFactory;


    @Override
    public Mono<InitialPlayerAccountItemsPage> getInitialAccountItemsPage(GetInitialPlayerAccountRequest.Filter filter, PaginationRequest paginationRequest) {
        GetInitialPlayerAccountRequest request = GetInitialPlayerAccountRequest.newBuilder()
                .setFilter(filter)
                .setPaginationRequest(paginationRequest)
                .build();

        return Mono.fromCallable(() ->
                        initialPlayerAccountServiceAPIBlockingStubObjectFactory.getObject().getInitialPlayerAccount(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC getInitialPlayerAccount start"))
                .doOnSuccess(resp -> log.info("gRPC getInitialPlayerAccount success"))
                .doOnError(e -> log.error("gRPC getInitialPlayerAccount error err={}", e.getMessage(), e));
    }

    @Override
    public Mono<Empty> updateInitialAccount(UpdateInitialPlayerAccountRequest request) {
        return Mono.fromCallable(() ->
                        initialPlayerAccountServiceAPIBlockingStubObjectFactory.getObject().updateInitialPlayerAccount(request))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSubscribe(s -> log.info("gRPC updateInitialPlayerAccount start"))
                .doOnSuccess(resp -> log.info("gRPC updateInitialPlayerAccount success"))
                .doOnError(e -> log.error("gRPC updateInitialPlayerAccount error err={}", e.getMessage(), e));
    }
}
//TODO logging
//TODO настроить пулы потоков для отказоустойчивости и производительности
//TODO возврат default в случае ошибки, но ошибку логировать. тоже для отказоусточивости. подумать, мб сделать везде