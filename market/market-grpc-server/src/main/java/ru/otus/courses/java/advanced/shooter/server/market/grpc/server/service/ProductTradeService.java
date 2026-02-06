package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.ProductTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;

import java.util.UUID;

public interface ProductTradeService {
    ProductTrade createProductTrade(@NotNull UUID playerUuid, int productId);

    ProductTrade getProductTrade(@NotNull UUID playerUuid, @NotNull UUID tradeUuid);

    Page<ProductTrade> getProductTrades(
            @NotNull @Valid ProductTradeFilterParams filterParams,
            @NotNull Pageable pageable
    );
}
