package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.ProductTradeStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_trade")
@IdClass(TradeId.class)
@Getter
@Setter
@FieldNameConstants
@ToString(exclude = {"priceCurrency", "product"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProductTrade {

    @Id
    @NotNull
    @Column(name = "player_uuid")
    @EqualsAndHashCode.Include
    private UUID playerUuid;

    @Id
    @NotNull
    @Column(name = "uuid")
    @EqualsAndHashCode.Include
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    @NotNull
    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "product_equipment_id", referencedColumnName = "equipment_id", insertable = false, updatable = false),
            @JoinColumn(name = "product_equipment_type", referencedColumnName = "equipment_type", insertable = false, updatable = false)
    })
    private ReferenceEquipment productEquipment;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = ReferenceEquipmentId.Fields.equipmentType,
                    column = @Column(name = "product_equipment_type")
            ),
            @AttributeOverride(
                    name = ReferenceEquipmentId.Fields.equipmentId,
                    column = @Column(name = "product_equipment_id")
            )
    })
    private ReferenceEquipmentId productEquipmentId;

    @Positive
    @Column(name = "equipment_amount")
    private int equipmentAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "price_currency_id", insertable = false, updatable = false)
    private ReferenceCurrency priceCurrency;

    @NotNull
    @Column(name = "price_currency_id")
    private Integer priceCurrencyId;

    @Positive
    @Column(name = "price_value")
    private int priceValue;

    @Version
    @Column(name = "version")
    private int version;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @NotNull
    @Column(name = "status_code")
    @Convert(converter = ProductTradeStatusConverter.class)
    private ProductTradeStatus status;
}

