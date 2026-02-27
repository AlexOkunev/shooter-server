package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

public interface ProductGrpcClient {

    ProductInfo getProduct(GetProductRequest request);

    ProductInfo getEnabledProduct(GetProductRequest request);

    ProductInfoListPage getProducts(GetProductsRequest request);

    ProductInfo createProduct(CreateProductRequest request);

    ProductInfo updateProduct(UpdateProductRequest request);
}
