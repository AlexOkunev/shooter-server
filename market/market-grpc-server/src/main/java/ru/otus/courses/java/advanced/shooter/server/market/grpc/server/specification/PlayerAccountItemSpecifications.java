package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.PlayerAccountItem;

@UtilityClass
public class PlayerAccountItemSpecifications {
    public static Specification<PlayerAccountItem> byPlayerId(int playerId) {
        return (root, query, builder) ->
                builder.equal(root.get(PlayerAccountItem.Fields.playerId), playerId);
    }

    public static Specification<PlayerAccountItem> byPositiveAmount() {
        return (root, query, builder) ->
                builder.gt(root.get(PlayerAccountItem.Fields.currencyAmount), 0);
    }
}