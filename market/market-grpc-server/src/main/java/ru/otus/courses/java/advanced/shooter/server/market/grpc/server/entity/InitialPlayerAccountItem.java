package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "initial_player_account_item")
@FieldNameConstants
public class InitialPlayerAccountItem {
    @Id
    @Column(name = "currency_id", updatable = false)
    private int currencyId;

    @NotNull
    @ManyToOne
    @MapsId
    @JoinColumn(name = "currency_id")
    private ReferenceCurrency currency;

    @Min(0)
    @Column(name = "amount")
    private int amount;

    @Column(name = "enabled")
    private boolean enabled;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @Version
    @Column(name = "version")
    private int version;
}
