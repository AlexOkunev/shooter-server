package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.InitialPlayerInventoryItem;

@UtilityClass
public class InitialPlayerInventorySpecifications {
    public static Specification<InitialPlayerInventoryItem> byEnabled(boolean enabled) {
        return (root, query, builder) ->
                builder.equal(root.get(InitialPlayerInventoryItem.Fields.enabled), enabled);
    }
}