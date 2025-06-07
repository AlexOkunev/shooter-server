package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.ProductEquipmentTypeConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.converter.ProductTradeStatusConverter;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductTradeStatus;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_trade")
@Data
@FieldNameConstants
public class ProductTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product_trade_gen")
    @SequenceGenerator(name = "seq_product_trade_gen", sequenceName = "seq_product_trade", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "player_id")
    private Integer playerId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "product_id", insertable = false, updatable = false)
    private Integer productId;

    @NotNull
    @ManyToOne/*(fetch = FetchType.LAZY)*/
    @JoinColumns({
            @JoinColumn(name = "product_equipment_id", referencedColumnName = "equipment_id"),
            @JoinColumn(name = "product_equipment_type", referencedColumnName = "equipment_type")
    })
    private ReferenceEquipment productEquipment;

    @Column(name = "product_equipment_id", insertable = false, updatable = false)
    private Integer productEquipmentId;

    @Column(name = "product_equipment_type", insertable = false, updatable = false)
    @Convert(converter = ProductEquipmentTypeConverter.class)
    private ProductEquipmentType productEquipmentType;

    @Positive
    @Column(name = "equipment_amount")
    private int equipmentAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_currency_id")
    private ReferenceCurrency priceCurrency;

    @Column(name = "price_currency_id", insertable = false, updatable = false)
    private Integer priceCurrencyId;

    @Positive
    @Column(name = "price_value")
    private int priceValue;

    @Version
    @Column(name = "version")
    private int version;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @NotNull
    @Column(name = "status_code")
    @Convert(converter = ProductTradeStatusConverter.class)
    private ProductTradeStatus status;

    @NotNull
    @Column(name = "uuid")
    private UUID uuid;
}

