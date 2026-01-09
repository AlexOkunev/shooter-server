package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.otus.courses.java.advanced.shooter.server.common.utils.exception.ObjectNotFoundException;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.bean.PlayersFilterParams;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.repository.PlayerRepository;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.specifications.PlayerSpecifications;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Override
    public Player getPlayerByPlayerUuid(UUID playerUuid) {
        return playerRepository.findByPlayerUuid(playerUuid)
                .orElseThrow(() -> new ObjectNotFoundException("Player with uuid '%s' not found".formatted(playerUuid)));
    }

    @Override
    public Player getPlayerByKeycloakId(String keycloakId) {
        return playerRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new ObjectNotFoundException("Player with keycloak ID '%s' not found".formatted(keycloakId)));
    }

    @Override
    public Page<Player> getPlayers(@NotNull PlayersFilterParams filterParams, @NotNull Pageable pageable) {
        return playerRepository.findAll(getPlayerSpecification(filterParams), pageable);
    }

    private static Specification<Player> getPlayerSpecification(PlayersFilterParams playersFilter) {
        List<Specification<Player>> specifications = new ArrayList<>();

        if (playersFilter.getEmail() != null) {
            specifications.add(PlayerSpecifications.byEmailStartsWith(playersFilter.getEmail()));
        }

        if (playersFilter.getLogin() != null) {
            specifications.add(PlayerSpecifications.byLoginStartsWith(playersFilter.getLogin()));
        }

        if (playersFilter.getEnabled() != null) {
            specifications.add(PlayerSpecifications.byEnabled(playersFilter.getEnabled()));
        }

        if (!playersFilter.getPlayerUuids().isEmpty()) {
            specifications.add(PlayerSpecifications.byPlayerIds(playersFilter.getPlayerUuids()));
        }

        return Specification.allOf(specifications);
    }
}
