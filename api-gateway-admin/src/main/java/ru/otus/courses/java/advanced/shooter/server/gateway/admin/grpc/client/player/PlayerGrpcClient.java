package ru.otus.courses.java.advanced.shooter.server.gateway.admin.grpc.client.player;

import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayersRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfoListPage;

public interface PlayerGrpcClient {

    PlayerInfo getPlayer(GetPlayerRequest request);

    PlayerInfoListPage getPlayers(GetPlayersRequest request);
}
