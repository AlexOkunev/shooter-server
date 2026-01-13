package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "initial_player_inventory_item")
@FieldNameConstants
public class InitialPlayerInventoryItem {
    @EmbeddedId
    private ReferenceEquipmentId id;

    @Positive
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
