package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;
import ru.otus.courses.java.advanced.shooter.server.market.grpc.server.enumeration.ProductEquipmentType;

import java.time.ZonedDateTime;

@Entity
@Table(name = "product")
@Data
@FieldNameConstants
public class Product implements CacheableData<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product_gen")
    @SequenceGenerator(name = "seq_product_gen", sequenceName = "seq_product", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "enabled")
    private boolean enabled = true;

    @NotNull
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "ref_equipment_id", referencedColumnName = "equipment_id"),
            @JoinColumn(name = "ref_equipment_type", referencedColumnName = "equipment_type")
    })
    private ReferenceEquipment equipment;

    @Column(name = "ref_equipment_id", insertable = false, updatable = false)
    private Integer equipmentId;

    @Column(name = "ref_equipment_type", insertable = false, updatable = false)
    private ProductEquipmentType equipmentType;

    @NotNull
    @Positive
    @Column(name = "equipment_amount")
    private Integer equipmentAmount;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "price_ref_currency_id")
    private ReferenceCurrency priceCurrency;

    @Column(name = "price_ref_currency_id", insertable = false, updatable = false)
    private Integer priceCurrencyId;

    @Positive
    @Column(name = "price")
    private int price;

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
}