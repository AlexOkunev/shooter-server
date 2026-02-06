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
@Table(name = "money_bundle")
@Getter
@Setter
@FieldNameConstants
@ToString(exclude = "currency")
public class MoneyBundle implements CacheableData<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_money_bundle_gen")
    @SequenceGenerator(name = "seq_money_bundle_gen", sequenceName = "seq_money_bundle", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "enabled")
    private boolean enabled = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private ReferenceCurrency currency;

    @NotNull
    @Column(name = "currency_id")
    private Integer currencyId;

    @Positive
    @Column(name = "currency_amount")
    private int currencyAmount;

    @Positive
    @Column(name = "rubles_price")
    private int rublesPrice;

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

        MoneyBundle that = (MoneyBundle) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public final int hashCode() {
        return Hibernate.getClass(this).hashCode();
    }
}