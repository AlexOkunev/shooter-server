package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayersRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfoListPage;

public interface PlayerService {
    PlayerInfo getPlayer(GetPlayerRequest request);

    PlayerInfoListPage getPlayers(GetPlayersRequest request);
}
