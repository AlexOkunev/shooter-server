package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import com.google.protobuf.Empty;
import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.PlayerAccountItemsPage;

public interface PlayerAccountService {

    Mono<PlayerAccountItemsPage> getPlayerAccountItemsPage(String playerUuid, PaginationRequest paginationRequest);

    Mono<PlayerAccountItemInfo> takeAwayCurrency(String playerUuid, int currencyId, int amount);

    Mono<PlayerAccountItemInfo> giveCurrency(String playerUuid, int currencyId, int amount);

    Mono<Empty> initializeAccount(String playerUuid);
}
