package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Entity
@Table(name = "initial_player_account_item")
@Getter
@Setter
@FieldNameConstants
@ToString(exclude = "currency")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InitialPlayerAccountItem {
    @Id
    @Column(name = "currency_id")
    @EqualsAndHashCode.Include
    private int currencyId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", updatable = false, insertable = false)
    private ReferenceCurrency currency;

    @Positive
    @Column(name = "amount")
    private int amount;

    @Column(name = "enabled")
    private boolean enabled;

    @CreationTimestamp
    @Column(name = "created_timestamp")
    private ZonedDateTime createdTimestamp;

    @UpdateTimestamp
    @Column(name = "updated_timestamp")
    private ZonedDateTime updatedTimestamp;

    @Version
    @Column(name = "version")
    private int version;
}
