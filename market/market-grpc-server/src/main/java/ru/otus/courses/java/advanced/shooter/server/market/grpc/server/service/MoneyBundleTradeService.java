package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleTradeFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;

import java.util.UUID;

public interface MoneyBundleTradeService {

    MoneyBundleTrade createMoneyBundleTrade(@NotNull UUID playerUuid, int moneyBundleId);

    MoneyBundleTrade getMoneyBundleTrade(@NotNull UUID playerUuid, @NotNull UUID tradeUuid);

    Page<MoneyBundleTrade> getMoneyBundleTrades(
            @NotNull @Valid MoneyBundleTradeFilterParams filterParams,
            @NotNull Pageable pageable
    );

    MoneyBundleTrade performMoneyBundleTradePayment(@NotNull UUID playerUuid, @NotNull UUID tradeUuid);
}
