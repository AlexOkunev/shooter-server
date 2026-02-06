package ru.otus.courses.java.advanced.shooter.server.market.grpc.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@Entity
@Table(name = "player_account_item")
@IdClass(PlayerAccountItemId.class)
@Getter
@Setter
@FieldNameConstants
@ToString(exclude = "currency")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PlayerAccountItem {

    @Id
    @Column(name = "player_uuid")
    @EqualsAndHashCode.Include
    private UUID playerUuid;

    @Id
    @Column(name = "currency_id")
    @EqualsAndHashCode.Include
    private Integer currencyId;

    @NotNull
    @Min(0)
    @Column(name = "currency_amount")
    private Integer amount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", insertable = false, updatable = false)
    private ReferenceCurrency currency;

    @Version
    @Column(name = "version")
    private int version;
}
