package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.common.mapping.core.mapper.PaginationInfoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.mapper.proto.ProductTradeProtoMapper;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.*;

import java.util.UUID;

@GRpcService
@RequiredArgsConstructor
public class ProductTradeServiceAPIImpl extends ProductTradeServiceAPIGrpc.ProductTradeServiceAPIImplBase {

    private final ProductTradeService productTradeService;
    private final PaginationInfoMapper paginationInfoMapper;
    private final ProductTradeProtoMapper productTradeProtoMapper;

    @Override
    public void createProductTrade(CreateProductTradeRequest request, StreamObserver<ProductTradeInfo> responseObserver) {
        ProductTrade productTrade = productTradeService.createProductTrade(
                UUID.fromString(request.getPlayerUuid()),
                request.getProductId()
        );

        ProductTradeInfo response = productTradeProtoMapper.toResponse(productTrade);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductTrade(GetProductTradeRequest request, StreamObserver<ProductTradeInfo> responseObserver) {
        ProductTrade productTrade = productTradeService.getProductTrade(
                UUID.fromString(request.getPlayerUuid()),
                UUID.fromString(request.getTradeUuid())
        );

        ProductTradeInfo response = productTradeProtoMapper.toResponse(productTrade);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getProductTrades(GetProductTradesRequest request, StreamObserver<ProductTradeInfoListPage> responseObserver) {
        Pageable pageable = productTradeProtoMapper.toPageable(request);
        ProductTradeFilterParams filterParams = productTradeProtoMapper.toFilterParams(request);

        Page<ProductTrade> data = productTradeService.getProductTrades(filterParams, pageable);

        ProductTradeInfoListPage response = ProductTradeInfoListPage.newBuilder()
                .addAllData(productTradeProtoMapper.toResponseList(data.getContent()))
                .setPaginationInfo(paginationInfoMapper.toResponse(data))
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}