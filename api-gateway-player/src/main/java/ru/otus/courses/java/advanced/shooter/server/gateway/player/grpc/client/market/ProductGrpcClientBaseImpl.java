package ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Component;
import ru.otus.courses.java.advanced.shooter.server.gateway.player.grpc.client.market.ProductGrpcClient;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

@Slf4j
@Component(ProductGrpcClientBaseImpl.NAME)
@RequiredArgsConstructor
public class ProductGrpcClientBaseImpl implements ProductGrpcClient {

    public static final String NAME = "productGrpcClientBaseImpl";

    private final ObjectFactory<ProductServiceAPIGrpc.ProductServiceAPIBlockingStub>
            productServiceAPIBlockingStubObjectFactory;

    @Override
    public ProductInfo getProduct(GetProductRequest request) {
        log.debug("Getting product with request: {}", request);
        return productServiceAPIBlockingStubObjectFactory.getObject().getProduct(request);
    }

    @Override
    public ProductInfo getEnabledProduct(GetProductRequest request) {
        log.debug("Getting enabled product with request: {}", request);
        return productServiceAPIBlockingStubObjectFactory.getObject().getEnabledProduct(request);
    }

    @Override
    public ProductInfoListPage getProducts(GetProductsRequest request) {
        log.debug("Getting products with request: {}", request);
        return productServiceAPIBlockingStubObjectFactory.getObject().getProducts(request);
    }

    @Override
    public ProductInfo createProduct(CreateProductRequest request) {
        log.debug("Creating product with request: {}", request);
        return productServiceAPIBlockingStubObjectFactory.getObject().createProduct(request);
    }

    @Override
    public ProductInfo updateProduct(UpdateProductRequest request) {
        log.debug("Updating product with request: {}", request);
        return productServiceAPIBlockingStubObjectFactory.getObject().updateProduct(request);
    }
}
