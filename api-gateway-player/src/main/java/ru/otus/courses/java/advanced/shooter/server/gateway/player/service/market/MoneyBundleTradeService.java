package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.GetMoneyBundleTradesRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.MoneyBundleTradeInfoListPage;

public interface MoneyBundleTradeService {

    Mono<MoneyBundleTradeInfo> fetchOne(String playerUuid, String tradeUuid);

    Mono<MoneyBundleTradeInfoListPage> fetchPage(String playerUuid, GetMoneyBundleTradesRequest.Filter filter, PaginationRequest paginationRequest);

    Mono<MoneyBundleTradeInfo> createTrade(String playerUuid, int moneyBundleId);

    Mono<MoneyBundleTradeInfo> performPayment(String playerUuid, String tradeUuid);
}
