package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.MoneyBundle;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;

@UtilityClass
public class MoneyBundleSpecifications {

    public static Specification<MoneyBundle> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(MoneyBundle.Fields.enabled), enabled);
    }

    public static Specification<MoneyBundle> byCurrencyEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.join(MoneyBundle.Fields.currency).get(ReferenceCurrency.Fields.enabled), enabled);
    }

    public static Specification<MoneyBundle> byCurrencyCanBeBought(boolean canBeBought) {
        return (root, query, builder) ->
                builder.equal(root.join(MoneyBundle.Fields.currency).get(ReferenceCurrency.Fields.canBeBought), canBeBought);
    }

    public static Specification<MoneyBundle> byCurrencyIds(Iterable<Integer> currencyIds) {
        return (root, query, builder) ->
                builder.in(root.get(MoneyBundle.Fields.currencyId)).value(currencyIds);
    }
}