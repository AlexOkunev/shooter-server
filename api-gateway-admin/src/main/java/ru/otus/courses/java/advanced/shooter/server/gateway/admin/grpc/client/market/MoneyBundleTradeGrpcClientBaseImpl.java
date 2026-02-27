package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.*;

@Slf4j
@Component(MoneyBundleTradeGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class MoneyBundleTradeGrpcClientBaseImpl implements MoneyBundleTradeGrpcClient {

    public static final String NAME = "moneyBundleTradeGrpcClientBaseImpl";

    private final ObjectFactory<MoneyBundleTradeServiceAPIGrpc.MoneyBundleTradeServiceAPIBlockingStub>
            moneyBundleTradeServiceAPIBlockingStubObjectFactory;

    @Override
    public MoneyBundleTradeInfo createMoneyBundleTrade(CreateMoneyBundleTradeRequest request) {
        log.debug("Creating money bundle trade with request: {}", request);
        return moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().createMoneyBundleTrade(request);
    }

    @Override
    public MoneyBundleTradeInfo performMoneyBundleTradePayment(PerformMoneyBundleTradePaymentRequest request) {
        log.debug("Performing money bundle trade payment with request: {}", request);
        return moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().performMoneyBundleTradePayment(request);
    }

    @Override
    public MoneyBundleTradeInfo getMoneyBundleTrade(GetMoneyBundleTradeRequest request) {
        log.debug("Getting money bundle trade with request: {}", request);
        return moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundleTrade(request);
    }

    @Override
    public MoneyBundleTradeInfoListPage getMoneyBundleTrades(GetMoneyBundleTradesRequest request) {
        log.debug("Getting money bundle trades with request: {}", request);
        return moneyBundleTradeServiceAPIBlockingStubObjectFactory.getObject().getMoneyBundleTrades(request);
    }
}
