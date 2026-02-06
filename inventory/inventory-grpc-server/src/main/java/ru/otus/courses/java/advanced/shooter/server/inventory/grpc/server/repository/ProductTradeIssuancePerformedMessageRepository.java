package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.outbox.PlayerBoundMessageId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.outbox.ProductTradeIssuancePerformedMessage;

import java.util.UUID;

@Repository
public interface ProductTradeIssuancePerformedMessageRepository
        extends JpaRepository<ProductTradeIssuancePerformedMessage, PlayerBoundMessageId> {

    boolean existsByPlayerUuidAndTradeUuidAndSuccess(UUID playerUuid, UUID tradeUuid, boolean success);
}
