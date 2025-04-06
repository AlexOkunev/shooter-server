package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter.EquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.converter.OperationTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.EquipmentType;
import ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.enumeration.OperationType;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "player_inventory_log_entry")
@FieldNameConstants
public class PlayerInventoryLogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pl_inv_log_entry_gen")
    @SequenceGenerator(name = "seq_pl_inv_log_entry_gen", sequenceName = "seq_pl_inv_log_entry", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "player_id")
    @NotNull
    private Integer playerId;

    @Column(name = "equipment_type")
    @Convert(converter = EquipmentTypeConverter.class)
    @NotNull
    private EquipmentType equipmentType;

    @Column(name = "equipment_id")
    @NotNull
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

    @Min(0)
    @NotNull
    @Column(name = "held_amount_before")
    private Integer heldAmountBefore;

    @Min(0)
    @NotNull
    @Column(name = "held_amount_after")
    private Integer heldAmountAfter;

    @NotNull
    @Column(name = "created_timestamp")
    private ZonedDateTime timestamp;
}
