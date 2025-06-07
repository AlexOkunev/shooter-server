package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundleTrade;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.MoneyBundleTradeStatus;

@UtilityClass
public class MoneyBundleTradeSpecifications {
    public static Specification<MoneyBundleTrade> byIds(Iterable<Integer> ids) {
        return (root, query, builder) ->
                builder.in(root.get(MoneyBundleTrade.Fields.id)).value(ids);
    }

    public static Specification<MoneyBundleTrade> byPlayerId(Integer playerId) {
        return (root, query, builder) ->
                builder.equal(root.get(MoneyBundleTrade.Fields.playerId), playerId);
    }

    public static Specification<MoneyBundleTrade> byCurrencyIds(Iterable<Integer> currencyIds) {
        return (root, query, builder) ->
                builder.in(root.get(MoneyBundleTrade.Fields.currencyId)).value(currencyIds);
    }

    public static Specification<MoneyBundleTrade> byMoneyBundleIds(Iterable<Integer> moneyBundleIds) {
        return (root, query, builder) ->
                builder.in(root.get(MoneyBundleTrade.Fields.moneyBundleId)).value(moneyBundleIds);
    }

    public static Specification<MoneyBundleTrade> byStatuses(Iterable<MoneyBundleTradeStatus> statuses) {
        return (root, query, builder) ->
                builder.in(root.get(MoneyBundleTrade.Fields.status)).value(statuses);
    }
}