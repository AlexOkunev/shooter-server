package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service;

import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayerResponse;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayersRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.GetPlayersResponse;

public interface PlayerService {
    GetPlayerResponse getPlayer(GetPlayerRequest request);

    GetPlayersResponse getPlayers(GetPlayersRequest request);
}
