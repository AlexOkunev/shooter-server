package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.bean.PlayerCurrencyOperationCommand;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;

import java.util.UUID;

public interface PlayerAccountService {
    Page<PlayerAccountItem> getPlayerAccountItems(@NotNull UUID playerUuid, boolean onlyEnabledCurrencies, @NotNull Pageable pageable);

    void initializePlayerAccount(@NotNull UUID playerUuid);

    void initializePlayerAccountBySystemEvent(@NotNull UUID playerUuid);

    void setPlayerAccountEmailBySystemEvent(@NotNull UUID playerUuid, @NotNull String email);

    PlayerAccountItem giveCurrency(@Valid @NotNull PlayerCurrencyOperationCommand command);

    PlayerAccountItem buyCurrency(@NotNull UUID tradeUuid, @Valid @NotNull PlayerCurrencyOperationCommand command);

    PlayerAccountItem takeAwayCurrency(@Valid @NotNull PlayerCurrencyOperationCommand command);

    void performCurrencyWriteOff(@NotNull UUID tradeUuid, @Valid @NotNull PlayerCurrencyOperationCommand command);

    void refundMoneyForProductTrade(@NotNull ProductTrade productTrade);
}
