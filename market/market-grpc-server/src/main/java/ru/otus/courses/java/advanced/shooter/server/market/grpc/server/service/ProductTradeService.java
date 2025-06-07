package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.*;

public interface ProductTradeService {
    ProductTradeInfo createProductTrade(CreateProductTradeRequest request);

    ProductTradeInfo getProductTrade(GetProductTradeRequest request);

    ProductTradeInfoListPage getProductTrades(GetProductTradesRequest request);
}
