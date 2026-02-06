package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleFilterParams;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.MoneyBundleSavedData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;

public interface MoneyBundleService {
    MoneyBundle getMoneyBundle(int id);

    MoneyBundle getEnabledMoneyBundle(int id);

    MoneyBundle createMoneyBundle(@Valid @NotNull MoneyBundleSavedData data);

    MoneyBundle updateMoneyBundle(
            int id,
            int currentVersion,
            @Valid @NotNull MoneyBundleSavedData data
    );

    Page<MoneyBundle> getMoneyBundles(
            @NotNull MoneyBundleFilterParams filterParams,
            @NotNull Pageable pageable
    );
}
