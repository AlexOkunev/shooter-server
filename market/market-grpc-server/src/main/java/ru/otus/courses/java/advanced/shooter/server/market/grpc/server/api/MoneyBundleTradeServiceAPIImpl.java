package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.MoneyBundleTradeProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.MoneyBundleTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.money.bundle.*;

import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class MoneyBundleTradeServiceAPIImpl extends MoneyBundleTradeServiceAPIGrpc.MoneyBundleTradeServiceAPIImplBase {

    private final MoneyBundleTradeService moneyBundleTradeService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final MoneyBundleTradeProtoMapper moneyBundleTradeProtoMapper;

    @Override
    public void createMoneyBundleTrade(CreateMoneyBundleTradeRequest request, StreamObserver<MoneyBundleTradeInfo> responseObserver) {
        MoneyBundleTrade trade = moneyBundleTradeService.createMoneyBundleTrade(
                UUID.fromString(request.getPlayerUuid()),
                request.getMoneyBundleId()
        );

        MoneyBundleTradeInfo response = moneyBundleTradeProtoMapper.toResponse(trade);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMoneyBundleTrade(GetMoneyBundleTradeRequest request, StreamObserver<MoneyBundleTradeInfo> responseObserver) {
        MoneyBundleTrade trade = moneyBundleTradeService.getMoneyBundleTrade(
                UUID.fromString(request.getPlayerUuid()),
                UUID.fromString(request.getTradeUuid())
        );

        MoneyBundleTradeInfo response = moneyBundleTradeProtoMapper.toResponse(trade);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMoneyBundleTrades(GetMoneyBundleTradesRequest request, StreamObserver<MoneyBundleTradeInfoListPage> responseObserver) {
        Pageable pageable = moneyBundleTradeProtoMapper.toPageable(request);
        MoneyBundleTradeFilterParams filterParams = moneyBundleTradeProtoMapper.toFilterParams(request);

        Page<MoneyBundleTrade> data = moneyBundleTradeService.getMoneyBundleTrades(filterParams, pageable);

        MoneyBundleTradeInfoListPage response = MoneyBundleTradeInfoListPage.newBuilder()
                .addAllData(moneyBundleTradeProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void performMoneyBundleTradePayment(PerformMoneyBundleTradePaymentRequest request, StreamObserver<MoneyBundleTradeInfo> responseObserver) {
        MoneyBundleTrade trade = moneyBundleTradeService.performMoneyBundleTradePayment(
                UUID.fromString(request.getPlayerUuid()),
                UUID.fromString(request.getTradeUuid())
        );

        MoneyBundleTradeInfo response = moneyBundleTradeProtoMapper.toResponse(trade);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
