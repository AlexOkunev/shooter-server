package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.TradeId;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public interface MoneyBundleTradeRepository extends JpaRepository<MoneyBundleTrade, TradeId>, JpaSpecificationExecutor<MoneyBundleTrade> {
    Optional<MoneyBundleTrade> findByPlayerUuidAndUuid(UUID playerUuid, UUID uuid);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
                update MoneyBundleTrade t
                   set t.status = :newStatus,
                       t.version = t.version + 1,
                       t.updatedTimestamp = :updatedTimestamp
                 where t.playerUuid = :playerUuid
                   and t.uuid = :uuid
                   and t.status = :expectedStatus
            """)
    int updateStatusChecked(
            @Param("playerUuid") UUID playerUuid,
            @Param("uuid") UUID uuid,
            @Param("expectedStatus") MoneyBundleTradeStatus expectedStatus,
            @Param("newStatus") MoneyBundleTradeStatus newStatus,
            @Param("updatedTimestamp") ZonedDateTime updatedTimestamp
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("""
             update MoneyBundleTrade t
                set t.status = :newStatus,
                    t.version = t.version + 1,
                    t.updatedTimestamp = :updatedTimestamp
              where t.status = :expectedStatus
                and t.updatedTimestamp <= :statusEnteredNotLaterThan
            """)
    int updateStatusForTrades(
            @Param("expectedStatus") MoneyBundleTradeStatus expectedStatus,
            @Param("newStatus") MoneyBundleTradeStatus newStatus,
            @Param("statusEnteredNotLaterThan") ZonedDateTime statusEnteredNotLaterThan,
            @Param("updatedTimestamp") ZonedDateTime updatedTimestamp
    );
}
