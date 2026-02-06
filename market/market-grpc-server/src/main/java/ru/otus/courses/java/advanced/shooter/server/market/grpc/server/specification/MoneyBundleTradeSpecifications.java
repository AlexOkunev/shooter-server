package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

import java.util.UUID;

@UtilityClass
public class MoneyBundleTradeSpecifications {

    public static Specification<MoneyBundleTrade> byPlayerUuid(UUID playerId) {
        return (root, query, builder) ->
                builder.equal(root.get(MoneyBundleTrade.Fields.playerUuid), playerId);
    }

    public static Specification<MoneyBundleTrade> byCurrencyIds(Iterable<Integer> currencyIds) {
        return (root, query, builder) ->
                builder.in(root.get(MoneyBundleTrade.Fields.currencyId)).value(currencyIds);
    }

    public static Specification<MoneyBundleTrade> byStatuses(Iterable<MoneyBundleTradeStatus> statuses) {
        return (root, query, builder) ->
                builder.in(root.get(MoneyBundleTrade.Fields.status)).value(statuses);
    }
}