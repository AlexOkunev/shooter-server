package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import com.google.protobuf.Empty;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.*;

public interface PlayerAccountGrpcClient {

    PlayerAccountItemsPage getPlayerAccount(GetPlayerAccountRequest request);

    Empty initializePlayerAccount(InitializePlayerAccountRequest request);

    PlayerAccountItemInfo giveCurrency(PlayerCurrencyOperationRequest request);

    PlayerAccountItemInfo takeAwayCurrency(PlayerCurrencyOperationRequest request);
}
