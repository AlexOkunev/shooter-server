package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity.outbox;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.ProductEquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "product_trade_issue_required_message_outbox")
@IdClass(PlayerBoundMessageId.class)
@FieldNameConstants
public class ProductTradeIssueRequiredMessage {

    @Id
    @Column(name = "player_uuid")
    private UUID playerUuid;

    @Id
    @Column(name = "message_uuid")
    private UUID uuid;

    @NotNull
    @Column(name = "trade_uuid")
    private UUID tradeUuid;

    @Column(name = "equipment_id")
    private Integer equipmentId;

    @Column(name = "equipment_type")
    @Convert(converter = ProductEquipmentTypeConverter.class)
    private ProductEquipmentType equipmentType;

    @Positive
    @Column(name = "equipment_amount")
    private int equipmentAmount;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;
}
