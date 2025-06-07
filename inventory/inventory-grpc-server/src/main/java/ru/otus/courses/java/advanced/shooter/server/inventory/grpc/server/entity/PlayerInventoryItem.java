package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter.InventoryEquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;

@Data
@Entity
@Table(name = "player_inventory_item")
@IdClass(PlayerInventoryItemId.class)
@FieldNameConstants
public class PlayerInventoryItem {
    @Id
    @Column(name = "player_id")
    private Integer playerId;

    @Id
    @Column(name = "equipment_type")
    @Convert(converter = InventoryEquipmentTypeConverter.class)
    private InventoryEquipmentType equipmentType;

    @Id
    @Column(name = "equipment_id")
    private int equipmentId;

    @Min(0)
    @Column(name = "amount")
    private int amount;

    @Min(0)
    @Column(name = "held_amount")
    private int heldAmount;

    @Version
    @Column(name = "version")
    private int version;
}
