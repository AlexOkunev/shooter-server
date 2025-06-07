package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.money.bundle.*;

@GRpcService
@RequiredArgsConstructor
public class MoneyBundleServiceAPIImpl extends MoneyBundleServiceAPIGrpc.MoneyBundleServiceAPIImplBase {
    private final MoneyBundleService moneyBundleService;

    @Override
    public void getMoneyBundle(GetMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        responseObserver.onNext(moneyBundleService.getMoneyBundle(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledMoneyBundle(GetMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        responseObserver.onNext(moneyBundleService.getEnabledMoneyBundle(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getMoneyBundles(GetMoneyBundlesRequest request, StreamObserver<MoneyBundleInfoListPage> responseObserver) {
        responseObserver.onNext(moneyBundleService.getMoneyBundles(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createMoneyBundle(CreateMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        responseObserver.onNext(moneyBundleService.createMoneyBundle(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateMoneyBundle(UpdateMoneyBundleRequest request, StreamObserver<MoneyBundleInfo> responseObserver) {
        responseObserver.onNext(moneyBundleService.updateMoneyBundle(request));
        responseObserver.onCompleted();
    }
}
