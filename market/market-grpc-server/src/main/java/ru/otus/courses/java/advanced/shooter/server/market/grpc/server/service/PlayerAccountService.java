package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

public interface PlayerAccountService {
    PlayerAccountItemsPage getPlayerAccount(GetPlayerAccountRequest request);

    void initializePlayerAccount(InitializePlayerAccountRequest request);

    PlayerAccountItemInfo giveCurrency(PlayerCurrencyOperationRequest request);

    PlayerAccountItemInfo takeAwayCurrency(PlayerCurrencyOperationRequest request);
}
