package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItemId;

import java.util.Optional;
import java.util.UUID;

public interface PlayerAccountItemRepository extends JpaRepository<PlayerAccountItem, PlayerAccountItemId>, JpaSpecificationExecutor<PlayerAccountItem> {
    Optional<PlayerAccountItem> findByPlayerUuidAndCurrencyId(UUID playerUuid, int currencyId);
}
