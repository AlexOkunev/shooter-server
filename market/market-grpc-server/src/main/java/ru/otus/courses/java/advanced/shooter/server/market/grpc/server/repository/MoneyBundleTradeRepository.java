package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;

import java.util.Optional;
import java.util.UUID;

public interface MoneyBundleTradeRepository extends JpaRepository<MoneyBundleTrade, Integer>, JpaSpecificationExecutor<MoneyBundleTrade> {
    Optional<MoneyBundleTrade> findByIdAndPlayerId(int id, int playerId);

    Optional<MoneyBundleTrade> findByUuid(UUID uuid);
}
