package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@Entity
@Table(name = "player_inventory_item")
@FieldNameConstants
public class PlayerInventoryItem {

    @EmbeddedId
    private PlayerInventoryItemId id;

    @Min(0)
    @Column(name = "amount")
    private int amount;

    @Version
    @Column(name = "version")
    private int version;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "equipment_id", referencedColumnName = "equipment_id", insertable = false, updatable = false),
            @JoinColumn(name = "equipment_type", referencedColumnName = "equipment_type", insertable = false, updatable = false)
    })
    private ReferenceEquipment referenceEquipment;
}
