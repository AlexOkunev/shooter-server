package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.specification;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;

import java.util.Collection;

@UtilityClass
public class PlayerInventoryItemSpecifications {
    public static Specification<PlayerInventoryItem> byPlayerId(int playerId) {
        return (root, query, builder) ->
                builder.equal(root.get(PlayerInventoryItem.Fields.playerId), playerId);
    }

    public static Specification<PlayerInventoryItem> byEquipmentTypeCodes(Collection<Integer> equipmentTypeCodes) {
        return (root, query, builder) ->
                builder.in(root.get(PlayerInventoryItem.Fields.equipmentType)).value(equipmentTypeCodes);
    }
}