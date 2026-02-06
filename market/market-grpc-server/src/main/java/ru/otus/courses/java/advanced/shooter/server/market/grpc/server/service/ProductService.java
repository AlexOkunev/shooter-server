package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.Product;

public interface ProductService {
    Product getProduct(int id);

    Product getEnabledProduct(int id);

    Product createProduct(@Valid @NotNull ProductSavedData data);

    Product updateProduct(
            int id,
            int version,
            @Valid @NotNull ProductSavedData data
    );

    Page<Product> getProducts(
            @NotNull ProductFilterParams filterParams,
            @NotNull Pageable pageable
    );
}
