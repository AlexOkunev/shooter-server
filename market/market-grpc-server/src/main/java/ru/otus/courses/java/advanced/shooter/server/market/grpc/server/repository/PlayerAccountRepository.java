package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccount;

import java.util.Optional;
import java.util.UUID;

public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, UUID> {
    Optional<PlayerAccount> findByPlayerUuid(UUID playerUuid);
}
