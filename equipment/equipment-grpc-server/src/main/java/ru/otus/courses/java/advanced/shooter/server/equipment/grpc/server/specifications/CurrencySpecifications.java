package ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.specifications;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.equipment.grpc.server.entity.Currency;

@UtilityClass
public class CurrencySpecifications {
    public static Specification<Currency> byNameStartsWith(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.get(Currency.Fields.name)), name.toLowerCase() + "%");
    }

    public static Specification<Currency> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(Currency.Fields.enabled), enabled);
    }

    public static Specification<Currency> byCanBeBought(boolean canBeBought) {
        return (root, query, builder) ->
                builder.equal(root.get(Currency.Fields.canBeBought), canBeBought);
    }

    public static Specification<Currency> byCanBeGivenAsAward(boolean canBeGivenAsAward) {
        return (root, query, builder) ->
                builder.equal(root.get(Currency.Fields.canBeGivenAsAward), canBeGivenAsAward);
    }

    public static Specification<Currency> byCurrencyIds(Iterable<Integer> currencyIds) {
        return (root, query, builder) ->
                builder.in(root.get(Currency.Fields.id)).value(currencyIds);
    }
}