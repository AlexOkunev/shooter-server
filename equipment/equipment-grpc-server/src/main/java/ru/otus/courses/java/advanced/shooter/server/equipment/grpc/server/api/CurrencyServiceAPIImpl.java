package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencyFilterParams;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.bean.CurrencySavedData;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.mapper.proto.CurrencyProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.service.CurrencyService;
import ru.otus.courses.java.advanced.shooter.server.equipment.protobuf.currency.*;

@GRpcService
@RequiredArgsConstructor
public class CurrencyServiceAPIImpl extends CurrencyServiceAPIGrpc.CurrencyServiceAPIImplBase {
    private final CurrencyService currencyService;
    private final CurrencyProtoMapper currencyProtoMapper;
    private final PaginationInfoMapper paginationInfoMapper;

    @Override
    public void getCurrency(GetCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        Currency currency = currencyService.getCurrency(request.getCurrencyId());
        CurrencyInfo response = currencyProtoMapper.toResponse(currency);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getEnabledCurrency(GetEnabledCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        Currency currency = currencyService.getEnabledCurrency(request.getCurrencyId());
        CurrencyInfo response = currencyProtoMapper.toResponse(currency);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCurrencies(GetCurrenciesRequest request, StreamObserver<CurrencyInfoListPage> responseObserver) {
        CurrencyFilterParams currencyFilterParams = currencyProtoMapper.toFilterParams(request);
        Pageable pageable = currencyProtoMapper.toPageable(request);
        Page<Currency> data = currencyService.getCurrencies(currencyFilterParams, pageable);

        CurrencyInfoListPage response = CurrencyInfoListPage.newBuilder()
                .addAllData(currencyProtoMapper.toResponseList(data))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createCurrency(CreateCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        CurrencySavedData currencySavedData = currencyProtoMapper.toSavedData(request);
        Currency currency = currencyService.createCurrency(currencySavedData);
        CurrencyInfo response = currencyProtoMapper.toResponse(currency);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCurrency(UpdateCurrencyRequest request, StreamObserver<CurrencyInfo> responseObserver) {
        CurrencySavedData currencySavedData = currencyProtoMapper.toSavedData(request);
        Currency currency = currencyService.updateCurrency(request.getCurrencyId(), currencySavedData);
        CurrencyInfo response = currencyProtoMapper.toResponse(currency);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
