package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.market;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfo;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.product.ProductWritableData;

public interface ProductService {

    Mono<ProductInfo> fetchOne(int id);

    Mono<ProductInfoListPage> fetchProductsPage(PaginationRequest paginationRequest);

    Mono<ProductInfo> create(ProductWritableData data);

    Mono<ProductInfo> update(int id, int version, ProductWritableData data);
}
