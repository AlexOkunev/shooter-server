package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.otus.courses.java.advanced.shooter.server.common.utils.cache.data.CacheableData;

import java.time.ZonedDateTime;

@Entity
@Table(name = "money_bundle")
@Data
@FieldNameConstants
public class MoneyBundle implements CacheableData<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_money_bundle_gen")
    @SequenceGenerator(name = "seq_money_bundle_gen", sequenceName = "seq_money_bundle", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @Column(name = "enabled")
    private boolean enabled = true;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private ReferenceCurrency currency;

    @Column(name = "currency_id", insertable = false, updatable = false)
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;
}