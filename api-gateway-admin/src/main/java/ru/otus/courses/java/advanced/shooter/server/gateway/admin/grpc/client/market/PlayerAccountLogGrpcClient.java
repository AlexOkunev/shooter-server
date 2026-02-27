package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.market;

import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.GetPlayerAccountLogRequest;
import ru.otus.courses.java.advanced.shooter.server.market.protobuf.account.log.PlayerAccountLogPage;

public interface PlayerAccountLogGrpcClient {

    PlayerAccountLogPage getPlayerAccountLog(GetPlayerAccountLogRequest request);
}
