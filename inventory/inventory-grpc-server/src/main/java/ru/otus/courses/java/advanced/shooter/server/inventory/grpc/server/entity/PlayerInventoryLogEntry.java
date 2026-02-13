package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter.InventoryEquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter.OperationTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.InventoryEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "player_inventory_log_entry")
@IdClass(PlayerInventoryLogEntryId.class)
@FieldNameConstants
public class PlayerInventoryLogEntry {
    @Id
    @Column(name = "player_uuid")
    @NotNull
    private UUID playerUuid;

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "operation_uuid")
    private UUID operationUuid;

    @NotNull
    @Column(name = "equipment_type")
    @Convert(converter = InventoryEquipmentTypeConverter.class)
    private InventoryEquipmentType equipmentType;

    @NotNull
    @Column(name = "equipment_id")
    private Integer equipmentId;

    @Column(name = "operation_type")
    @Convert(converter = OperationTypeConverter.class)
    private OperationType operationType;

    @Min(0)
    @NotNull
    @Column(name = "amount_before")
    private Integer amountBefore;

    @Min(0)
    @NotNull
    @Column(name = "amount_after")
    private Integer amountAfter;

    @Column(name = "created_timestamp")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime timestamp;
}
