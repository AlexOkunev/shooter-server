package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.bean.PlayersFilterParams;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;

import java.util.UUID;

public interface PlayerService {

    Player getPlayerByPlayerUuid(UUID playerUuid);

    Player getPlayerByKeycloakId(String keycloakId);

    Page<Player> getPlayers(@NotNull PlayersFilterParams filterParams, @NotNull Pageable pageable);
}
