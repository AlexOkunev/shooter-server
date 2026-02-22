package ru.otus.courses.java.advanced.shooter.server.gateway.admin.service.player;

import reactor.core.publisher.Mono;
import ru.otus.courses.java.advanced.shooter.server.common.protobuf.PaginationRequest;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfo;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayerInfoListPage;
import ru.otus.courses.java.advanced.shooter.server.players.protobuf.PlayersFilter;

public interface PlayerService {

    Mono<PlayerInfo> getPlayerInfo(String uuid);

    Mono<PlayerInfoListPage> searchPlayers(PlayersFilter requestFilter, PaginationRequest paginationRequest);
}
