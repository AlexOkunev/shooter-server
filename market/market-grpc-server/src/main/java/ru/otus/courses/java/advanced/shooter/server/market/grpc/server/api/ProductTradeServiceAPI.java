package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.api;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service.ProductTradeService;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.*;

@GRpcService
@RequiredArgsConstructor
public class ProductTradeServiceAPI extends ProductTradeServiceAPIGrpc.ProductTradeServiceAPIImplBase {
    private final ProductTradeService productTradeService;

    @Override
    public void createProductTrade(CreateProductTradeRequest request, StreamObserver<ProductTradeInfo> responseObserver) {
        responseObserver.onNext(productTradeService.createProductTrade(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getProductTrade(GetProductTradeRequest request, StreamObserver<ProductTradeInfo> responseObserver) {
        responseObserver.onNext(productTradeService.getProductTrade(request));
        responseObserver.onCompleted();
    }

    @Override
    public void getProductTrades(GetProductTradesRequest request, StreamObserver<ProductTradeInfoListPage> responseObserver) {
        responseObserver.onNext(productTradeService.getProductTrades(request));
        responseObserver.onCompleted();
    }
}
