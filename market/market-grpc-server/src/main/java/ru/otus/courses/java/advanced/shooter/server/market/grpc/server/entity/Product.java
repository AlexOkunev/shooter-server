package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;

import java.time.ZonedDateTime;

@Entity
@Table(name = "product")
@Getter
@Setter
@FieldNameConstants
@ToString(exclude = {"priceCurrency", "equipment"})
public class Product implements CacheableData<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_product_gen")
    @SequenceGenerator(name = "seq_product_gen", sequenceName = "seq_product", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "enabled")
    private boolean enabled = true;

    //TODO возможно, вернуть, использовать entity graph    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "ref_equipment_id", referencedColumnName = "equipment_id", insertable = false, updatable = false),
            @JoinColumn(name = "ref_equipment_type", referencedColumnName = "equipment_type", insertable = false, updatable = false)
    })
    private ReferenceEquipment equipment;

    @NotNull
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = ReferenceEquipmentId.Fields.equipmentType,
                    column = @Column(name = "ref_equipment_type")
            ),
            @AttributeOverride(
                    name = ReferenceEquipmentId.Fields.equipmentId,
                    column = @Column(name = "ref_equipment_id")
            )
    })
    private ReferenceEquipmentId equipmentId;

    @NotNull
    @Positive
    @Column(name = "equipment_amount")
    private int equipmentAmount;

    //TODO возможно, вернуть, использовать entity graph    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne(optional = false)
    @JoinColumn(name = "price_ref_currency_id", insertable = false, updatable = false)
    private ReferenceCurrency priceCurrency;

    @NotNull
    @Column(name = "price_ref_currency_id")
    private Integer priceCurrencyId;

    @Positive
    @Column(name = "price")
    private int price;

    @Version
    @Column(name = "version")
    private int version;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @Override
    public final boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        Product that = (Product) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}