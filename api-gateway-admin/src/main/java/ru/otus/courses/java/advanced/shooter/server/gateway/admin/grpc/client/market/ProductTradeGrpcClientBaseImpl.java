package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.trade.product.*;

@Slf4j
@Component(ProductTradeGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class ProductTradeGrpcClientBaseImpl implements ProductTradeGrpcClient {

    public static final String NAME = "productTradeGrpcClientBaseImpl";

    private final ObjectFactory<ProductTradeServiceAPIGrpc.ProductTradeServiceAPIBlockingStub>
            productTradeServiceAPIBlockingStubObjectFactory;

    @Override
    public ProductTradeInfo createProductTrade(CreateProductTradeRequest request) {
        log.debug("Creating product trade with request: {}", request);
        return productTradeServiceAPIBlockingStubObjectFactory.getObject().createProductTrade(request);
    }

    @Override
    public ProductTradeInfo getProductTrade(GetProductTradeRequest request) {
        log.debug("Getting product trade with request: {}", request);
        return productTradeServiceAPIBlockingStubObjectFactory.getObject().getProductTrade(request);
    }

    @Override
    public ProductTradeInfoListPage getProductTrades(GetProductTradesRequest request) {
        log.debug("Getting product trades with request: {}", request);
        return productTradeServiceAPIBlockingStubObjectFactory.getObject().getProductTrades(request);
    }
}
