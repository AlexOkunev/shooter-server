package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.*;

public interface ProductService {
    ProductInfo getProduct(GetProductRequest request);

    ProductInfoListPage getProducts(GetProductsRequest request);

    ProductInfo createProduct(CreateProductRequest request);

    ProductInfo updateProduct(UpdateProductRequest request);

    ProductInfo getEnabledProduct(GetProductRequest request);
}
