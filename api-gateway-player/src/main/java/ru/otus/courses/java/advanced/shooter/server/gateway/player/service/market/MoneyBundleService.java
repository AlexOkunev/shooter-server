package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.MoneyBundleInfoListPage;

public interface MoneyBundleService {

    Mono<MoneyBundleInfo> fetchOne(int id);

    Mono<MoneyBundleInfoListPage> fetchMoneyBundlesPage(PaginationRequest paginationRequest);

    Mono<MoneyBundleInfoListPage> fetchMoneyBundlesPageByCurrencyId(int currencyId, PaginationRequest paginationRequest);
}
