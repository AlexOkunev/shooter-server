package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;

import java.util.Optional;

public interface PlayerAccountItemRepository extends JpaRepository<PlayerAccountItem, Integer>, JpaSpecificationExecutor<PlayerAccountItem> {
    Page<PlayerAccountItem> findByPlayerId(int playerId, Pageable pageable);

    Optional<PlayerAccountItem> findByPlayerIdAndCurrencyId(int playerId, int currencyId);
}
