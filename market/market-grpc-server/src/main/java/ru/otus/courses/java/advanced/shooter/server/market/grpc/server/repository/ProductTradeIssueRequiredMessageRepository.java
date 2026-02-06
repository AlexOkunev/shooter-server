package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.outbox.ProductTradeIssueRequiredMessage;

@Repository
public interface ProductTradeIssueRequiredMessageRepository extends JpaRepository<ProductTradeIssueRequiredMessage, Integer> {
}
