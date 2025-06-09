package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.*;

@GRpcService
@RequiredArgsConstructor
public class MoneyBundleTradeServiceAPIImpl extends MoneyBundleTradeServiceAPIGrpc.MoneyBundleTradeServiceAPIImplBase {
    private final MoneyBundleTradeService moneyBundleTradeService;

    @Override
    public void createMoneyBundleTrade(CreateMoneyBundleTradeRequest request, StreamObserver<MoneyBundleTradeInfo> responseObserver) {
        responseObserver.onNext(moneyBundleTradeService.createMoneyBundleTrade(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getMoneyBundleTrade(GetMoneyBundleTradeRequest request, StreamObserver<MoneyBundleTradeInfo> responseObserver) {
        responseObserver.onNext(moneyBundleTradeService.getMoneyBundleTrade(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getMoneyBundleTrades(GetMoneyBundleTradesRequest request, StreamObserver<MoneyBundleTradeInfoListPage> responseObserver) {
        responseObserver.onNext(moneyBundleTradeService.getMoneyBundleTrades(request));
        responseObserver.onCompleted();
    }

    @Override
    public void makeMoneyBundleTradePayment(MakeMoneyBundleTradePaymentRequest request, StreamObserver<MoneyBundleTradeInfo> responseObserver) {
        responseObserver.onNext(moneyBundleTradeService.makeMoneyBundleTradePayment(request));
        responseObserver.onCompleted();
    }
}
