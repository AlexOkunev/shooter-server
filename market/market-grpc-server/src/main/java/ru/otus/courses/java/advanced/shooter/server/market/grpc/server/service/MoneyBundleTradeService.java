package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.*;

public interface MoneyBundleTradeService {
    MoneyBundleTradeInfo createMoneyBundleTrade(CreateMoneyBundleTradeRequest request);

    MoneyBundleTradeInfo getMoneyBundleTrade(GetMoneyBundleTradeRequest request);

    MoneyBundleTradeInfoListPage getMoneyBundleTrades(GetMoneyBundleTradesRequest request);

    MoneyBundleTradeInfo makeMoneyBundleTradePayment(MakeMoneyBundleTradePaymentRequest request);
}
