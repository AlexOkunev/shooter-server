package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.*;

public interface ProductTradeGrpcClient {

    ProductTradeInfo createProductTrade(CreateProductTradeRequest request);

    ProductTradeInfo getProductTrade(GetProductTradeRequest request);

    ProductTradeInfoListPage getProductTrades(GetProductTradesRequest request);
}
