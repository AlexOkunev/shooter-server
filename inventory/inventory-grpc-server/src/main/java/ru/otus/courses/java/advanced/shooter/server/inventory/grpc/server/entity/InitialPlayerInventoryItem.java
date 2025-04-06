package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter.EquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "initial_player_inventory_item")
@IdClass(InitialPlayerInventoryItemId.class)
@FieldNameConstants
public class InitialPlayerInventoryItem {
    @Id
    @Column(name = "equipment_type")
    @Convert(converter = EquipmentTypeConverter.class)
    private EquipmentType equipmentType;

    @Id
    @Column(name = "equipment_id")
    private int equipmentId;

    @Min(0)
    @Column(name = "amount")
    private int amount;

    @Column(name = "enabled")
    private boolean enabled;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @Version
    @Column(name = "version")
    private int version;
}
