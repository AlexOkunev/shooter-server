package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccount;

import java.util.Optional;

public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, Integer> {
    Optional<PlayerAccount> findByPlayerId(Integer playerId);
}
