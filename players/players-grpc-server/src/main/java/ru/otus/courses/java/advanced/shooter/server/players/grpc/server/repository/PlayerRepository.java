package ru.otus.courses.java.advanced.shooter.server.players.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.players.grpc.server.entity.Player;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String>, JpaSpecificationExecutor<Player> {

    Optional<Player> findByPlayerUuid(UUID playerUuid);

    Optional<Player> findByKeycloakId(String keycloakId);
}
