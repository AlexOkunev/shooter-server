package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;

public interface PlayerAccountService {

    Mono<PlayerAccountItemsPage> getPlayerAccountItemsPage(String playerUuid, PaginationRequest paginationRequest);
}
