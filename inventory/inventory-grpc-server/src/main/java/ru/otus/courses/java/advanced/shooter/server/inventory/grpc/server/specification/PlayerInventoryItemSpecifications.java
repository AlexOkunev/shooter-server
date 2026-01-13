package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.specification;

import jakarta.persistence.criteria.JoinType;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItem;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.PlayerInventoryItemId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipment;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.ReferenceEquipmentId;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

import java.util.Collection;
import java.util.UUID;

@UtilityClass
public class PlayerInventoryItemSpecifications {

    public static Specification<PlayerInventoryItem> byPlayerUuid(UUID playerUuid) {
        return (root, query, builder) ->
                builder.equal(
                        root.get(PlayerInventoryItem.Fields.id)
                                .get(PlayerInventoryItemId.Fields.playerUuid),
                        playerUuid
                );
    }

    public static Specification<PlayerInventoryItem> byEquipmentTypes(Collection<InventoryEquipmentType> equipmentTypes) {
        return (root, query, builder) ->
                builder.in(root.get(PlayerInventoryItem.Fields.id)
                                .get(PlayerInventoryItemId.Fields.referenceEquipmentId)
                                .get(ReferenceEquipmentId.Fields.equipmentType))
                        .value(equipmentTypes);
    }

    public static Specification<PlayerInventoryItem> byReferenceEquipmentEnabled(Boolean enabled) {
        return (root, query, builder) ->
                builder.equal(
                        root.join(PlayerInventoryItem.Fields.referenceEquipment, JoinType.INNER)
                                .get(ReferenceEquipment.Fields.enabled),
                        enabled
                );
    }
}