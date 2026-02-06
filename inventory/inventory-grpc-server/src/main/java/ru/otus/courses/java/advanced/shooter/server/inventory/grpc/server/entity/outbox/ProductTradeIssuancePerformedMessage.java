package ru.otus.courses.java.advanced.shooter.server.inventory.grpc.server.entity.outbox;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_trade_issuance_performed_message_outbox")
@Data
@FieldNameConstants
@IdClass(PlayerBoundMessageId.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTradeIssuancePerformedMessage {

    @Id
    @NotNull
    @Column(name = "player_uuid")
    private UUID playerUuid;

    @Id
    @NotNull
    @Column(name = "message_uuid")
    private UUID messageUuid;

    @NotNull
    @Column(name = "trade_uuid")
    private UUID tradeUuid;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @Column(name = "success")
    private boolean success;
}
