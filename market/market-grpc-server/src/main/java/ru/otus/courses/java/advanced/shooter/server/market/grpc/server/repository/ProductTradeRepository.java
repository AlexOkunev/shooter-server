package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ProductTrade;

import java.util.Optional;
import java.util.UUID;

public interface ProductTradeRepository extends JpaRepository<ProductTrade, Integer>, JpaSpecificationExecutor<ProductTrade> {
    Optional<ProductTrade> findByPlayerUuidAndUuid(UUID playerUuid, UUID uuid);
}
