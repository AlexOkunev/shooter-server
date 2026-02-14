package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.OperationTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.OperationType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "player_account_log_entry")
@IdClass(PlayerAccountLogEntryId.class)
@FieldNameConstants
public class PlayerAccountLogEntry {

    @Id
    @Column(name = "player_uuid")
    @NotNull
    private UUID playerUuid;

    @Id
    @Column(name = "uuid")
    @NotNull
    private UUID uuid;

    @Column(name = "operation_uuid")
    private UUID operationUuid;

    @NotNull
    @Column(name = "currency_id")
    private Integer currencyId;

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
    @Temporal(TemporalType.TIMESTAMP)
    private ZonedDateTime timestamp;
}
