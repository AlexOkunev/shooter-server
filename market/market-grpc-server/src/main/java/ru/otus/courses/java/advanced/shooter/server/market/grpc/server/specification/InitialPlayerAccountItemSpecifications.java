package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.InitialPlayerAccountItem;

@UtilityClass
public class InitialPlayerAccountItemSpecifications {
    public static Specification<InitialPlayerAccountItem> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(InitialPlayerAccountItem.Fields.enabled), enabled);
    }
}