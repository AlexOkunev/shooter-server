package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.ReferenceCurrency;

import java.util.UUID;

@UtilityClass
public class PlayerAccountItemSpecifications {
    public static Specification<PlayerAccountItem> byPlayerUuid(UUID playerUuid) {
        return (root, query, builder) ->
                builder.equal(root.get(PlayerAccountItem.Fields.playerUuid), playerUuid);
    }

    public static Specification<PlayerAccountItem> byPositiveAmount() {
        return (root, query, builder) ->
                builder.gt(root.get(PlayerAccountItem.Fields.amount), 0);
    }

    public static Specification<PlayerAccountItem> byEnabledCurrency() {
        return (root, query, builder) ->
                builder.isTrue(root.join(PlayerAccountItem.Fields.currency)
                        .get(ReferenceCurrency.Fields.enabled)
                );
    }
}