package ru.otus.courses.java.advanced.shooter.server.gateway.player.service.market;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.GetProductTradesRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.ProductTradeInfoListPage;

public interface ProductTradeService {

    Mono<ProductTradeInfo> fetchOne(String playerUuid, String tradeUuid);

    Mono<ProductTradeInfoListPage> fetchPage(String playerUuid, GetProductTradesRequest.Filter filter, PaginationRequest paginationRequest);

    Mono<ProductTradeInfo> createTrade(String playerUuid, int productId);
}
