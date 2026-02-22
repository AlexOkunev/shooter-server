package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import com.google.protobuf.Empty;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.GetInitialPlayerAccountRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.InitialPlayerAccountItemsPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.initial.UpdateInitialPlayerAccountRequest;

public interface InitialPlayerAccountService {

    Mono<InitialPlayerAccountItemsPage> getInitialAccountItemsPage(GetInitialPlayerAccountRequest.Filter filter, PaginationRequest paginationRequest);

    Mono<Empty> updateInitialAccount(UpdateInitialPlayerAccountRequest request);
}
