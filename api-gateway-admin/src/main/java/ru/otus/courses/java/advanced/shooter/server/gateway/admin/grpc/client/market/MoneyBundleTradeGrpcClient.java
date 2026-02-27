package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.*;

public interface MoneyBundleTradeGrpcClient {

    MoneyBundleTradeInfo createMoneyBundleTrade(CreateMoneyBundleTradeRequest request);

    MoneyBundleTradeInfo performMoneyBundleTradePayment(PerformMoneyBundleTradePaymentRequest request);

    MoneyBundleTradeInfo getMoneyBundleTrade(GetMoneyBundleTradeRequest request);

    MoneyBundleTradeInfoListPage getMoneyBundleTrades(GetMoneyBundleTradesRequest request);
}
