package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.CurrencyService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;

@GRpcService
@RequiredArgsConstructor
public class CurrencyServiceAPIImpl extends CurrencyServiceAPIGrpc.CurrencyServiceAPIImplBase {
    private final CurrencyService currencyService;

    @Override
    public void getCurrency(GetCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        responseObserver.onNext(currencyService.getCurrencyInfo(request.getCurrencyId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledCurrency(GetEnabledCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        responseObserver.onNext(currencyService.getEnabledCurrencyInfo(request.getCurrencyId()));
        responseObserver.onCompleted();
    }

    @Override
    public void getCurrencies(GetCurrenciesRequest request, StreamObserver<CurrencyInfoListPage> responseObserver) {
        responseObserver.onNext(currencyService.getCurrencies(request));
        responseObserver.onCompleted();
    }

    @Override
    public void createCurrency(CreateCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        responseObserver.onNext(currencyService.createCurrency(request));
        responseObserver.onCompleted();
    }

    @Override
    public void updateCurrency(UpdateCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        responseObserver.onNext(currencyService.updateCurrency(request));
        responseObserver.onCompleted();
    }
}
